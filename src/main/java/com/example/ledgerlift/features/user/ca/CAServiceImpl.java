package com.example.ledgerlift.features.user.ca;

import com.example.ledgerlift.features.user.ca.dto.CAEnrollmentRequest;

import com.example.ledgerlift.utils.FabricUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Properties;
import java.util.Set;

@Service
@Slf4j
public class CAServiceImpl implements CAService {

    @Value("${fabric.ca.org1.caUrl}")
    private String org1CaUrl;

    @Value("${fabric.ca.org1.certificatePath}")
    private String org1CertificatePath;

    @Value("${fabric.wallet.config-path}")
    private String walletPath;
    private Wallet wallet;

    @Value("${fabric.ca.tls.enabled}")
    private Boolean tlsEnabled;

    @Value("${fabric.ca.admin.username}")
    private String adminUsername;

    @Value("${fabric.ca.admin.password}")
    private String adminPassword;

    @PostConstruct
    public void init() throws Exception {

        // Initialize wallet
        this.wallet = Wallets.newFileSystemWallet(Paths.get(walletPath));

        // check if admin identity exists in the wallet
        if (!FabricUtils.checkIdentityExistence("admin", wallet)) {
            log.info("Admin identity does not exist in the waller. Creating admin identity...");
            createAdminUserOrg1();
            CAEnrollmentRequest request = CAEnrollmentRequest.builder()
                    .username("client1")
                    .affiliation("org1.department")
                    .type("client")
                    .secret("admin@123")
                    .registrarUsername("admin")
                    .build();
            registerAndEnrollUser(request);
        } else {
            log.info("Admin identity already exists in the wallet.");
        }

    }

    @Override
    public void registerAndEnrollUser(CAEnrollmentRequest request) throws Exception{

        // HFCA client
        Properties props = new Properties();
        FabricUtils.setTlsProps(
                props,
                org1CertificatePath,
                tlsEnabled
        );

        HFCAClient hfcaClient = HFCAClient.createNewInstance(org1CaUrl, props);
        hfcaClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

        // 1. Register
        RegistrationRequest registrationRequest = new RegistrationRequest(request.getUsername());
        registrationRequest.setAffiliation(registrationRequest.getAffiliation());
        registrationRequest.setType("client");
        // auto generate the secret
        registrationRequest.setSecret(registrationRequest.getSecret());
        registrationRequest.setMaxEnrollments(-1); // unlimited enrollments

        Identity adminIdentity = wallet.get("admin");
        if (adminIdentity == null) {
            throw new IllegalArgumentException("admin identity has not been found");
        }

        // 1.1 Get the registrar
        // 1.1 getting the registrar
        User adminUser = new User() {

            final X509Identity x509Identity = (X509Identity) adminIdentity;

            @Override
            public String getName() {
                return "admin";
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return request.getAffiliation();
            }

            @Override
            public Enrollment getEnrollment() {
                return new Enrollment() {

                    @Override
                    public PrivateKey getKey() {
                        return x509Identity.getPrivateKey();
                    }

                    @Override
                    public String getCert() {
                        return Identities.toPemString(x509Identity.getCertificate());
                    }

                };
            }

            @Override
            public String getMspId() {
                return "Org1MSP";
            }

        };

        // 1.2 Register the user
        hfcaClient.register(registrationRequest, adminUser);
        log.info("Registered user: {} ", request.getUsername());

        // 2. Enroll
        EnrollmentRequest enrollmentRequest = new EnrollmentRequest();
        enrollmentRequest.setProfile("tls");
        enrollmentRequest.addHost("localhost");

        Enrollment enrollment = hfcaClient.enroll(
                request.getUsername(),
                request.getSecret(),
                enrollmentRequest
        );

        // 3. Add to wallet
        var certificate = Identities.readX509Certificate(
                enrollment.getCert()
        );
        Identity userIdentity = Identities.newX509Identity(
                "Org1MSP", certificate, enrollment.getKey()
        );

        wallet.put("client1", userIdentity);

    }

    private void createAdminUserOrg1() throws Exception {
        if (wallet.get("admin") != null) {
            log.info("Admin identity already exists in wallet.");
            return;
        }

        log.info("Creating admin identity...");
        var enrollmentRequest = new EnrollmentRequest();
        enrollmentRequest.setProfile("tls");
        enrollmentRequest.addHost("localhost");

        var caClient = createCAClient();
        var enrollment = caClient.enroll(adminUsername, adminPassword, enrollmentRequest);

        var certificate = Identities.readX509Certificate(enrollment.getCert());
        var adminIdentity = Identities.newX509Identity("Org1MSP", certificate, enrollment.getKey());

        wallet.put("admin", adminIdentity);
        log.info("Successfully stored admin identity in the wallet.");
    }

    private HFCAClient createCAClient() throws Exception {
        var props = new Properties();
        FabricUtils.setTlsProps(props, org1CertificatePath, tlsEnabled);
        var caClient = HFCAClient.createNewInstance(org1CaUrl, props);
        caClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        return caClient;
    }


}
