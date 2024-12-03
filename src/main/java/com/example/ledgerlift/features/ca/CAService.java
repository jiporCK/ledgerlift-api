package com.example.ledgerlift.features.ca;

import com.example.ledgerlift.features.ca.dto.CAEnrollmentRequest;
import com.example.ledgerlift.utils.FabricUtils;
import com.example.ledgerlift.utils.Utils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.gateway.X509Identity;
import org.hyperledger.fabric.sdk.Enrollment;
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
@RequiredArgsConstructor
public class CAService {

    // Constants for organization MSP and profile
    private static final String ORG1_MSP = "Org1MSP";
    private static final String PROFILE_TLS = "tls";
    private static final String ADMIN_LABEL = "admin";
    private static final String DONOR_LABEL = "donor";

    @Value("${fabric.ca.org1.caUrl}")
    private String org1CaUrl;

    @Value("${fabric.ca.org1.certificatePath}")
    private String org1CertificatePath;

    @Value("${fabric.wallet.config-path}")
    private String walletPath;

    @Value("${fabric.ca.tls.enabled}")
    private Boolean tlsEnabled;

    @Value("${fabric.ca.admin.username}")
    private String adminUsername;

    @Value("${fabric.ca.admin.password}")
    private String adminPassword;

    private Wallet wallet;

    @PostConstruct
    public void init() throws Exception {
        // Initialize the wallet to store user identities
        wallet = Wallets.newFileSystemWallet(Paths.get(walletPath));

        // Check if the admin identity exists in the wallet
        if (!FabricUtils.checkIdentityExistence(ADMIN_LABEL, wallet)) {
            log.info("Admin identity not found in wallet. Creating admin identity.");
            createAdminUser();  // Create and store the admin identity
            registerAndEnrollDefaultAdmin();  // Register and enroll the default admin user
        } else {
            log.info("Admin identity already exists in the wallet.");
        }
    }

    /**
     * Create the admin user and store its identity in the wallet.
     */
    private void createAdminUser() throws Exception {
        // Setup the Fabric CA client
        HFCAClient caClient = setupHFCAClient();

        // Enroll the admin user with the CA
        Enrollment enrollment = caClient.enroll(adminUsername, adminPassword, new EnrollmentRequest());

        // Read the certificate and store the admin identity in the wallet
        var certificate = Identities.readX509Certificate(enrollment.getCert());
        var adminIdentity = Identities.newX509Identity(ORG1_MSP, certificate, enrollment.getKey());

        wallet.put(ADMIN_LABEL, adminIdentity);
        log.info("Admin identity stored in wallet.");
    }

    /**
     * Set up the Hyperledger Fabric CA client with TLS properties.
     */
    private HFCAClient setupHFCAClient() throws Exception {
        Properties props = new Properties();
        FabricUtils.setTlsProps(props, org1CertificatePath, tlsEnabled);

        HFCAClient caClient = HFCAClient.createNewInstance(org1CaUrl, props);
        caClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        return caClient;
    }

    /**
     * Retrieve the admin user from the wallet and return it as a Fabric SDK User object.
     */
    private org.hyperledger.fabric.sdk.User getAdminUser() throws Exception {
        var adminIdentity = wallet.get(ADMIN_LABEL);
        if (adminIdentity == null) {
            throw new IllegalStateException("Admin identity not found in wallet.");
        }

        X509Identity x509Identity = (X509Identity) adminIdentity;

        return new org.hyperledger.fabric.sdk.User() {
            @Override
            public String getName() {
                return ADMIN_LABEL;
            }

            @Override
            public Set<String> getRoles() {
                return null;  // No roles specified
            }

            @Override
            public String getAccount() {
                return null;  // No account specified
            }

            @Override
            public String getAffiliation() {
                return "org1.department1";
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
                return ORG1_MSP;
            }
        };
    }

    /**
     * Register and enroll the default admin user.
     */
    private void registerAndEnrollDefaultAdmin() throws Exception {
        CAEnrollmentRequest request = CAEnrollmentRequest.builder()
                .username("jipor")
                .affiliation("org1.department1")
                .type("adminOrg")
                .secret(Utils.generateUuid())  // Generate a random secret for the admin user
                .registrarUsername(ADMIN_LABEL)
                .genSecret(true)
                .build();

        registerAndEnrollUser(request.getUsername(), request);
    }

    /**
     * Register and enroll a user with the specified enrollment request.
     */
    public void registerAndEnrollUser(String username, CAEnrollmentRequest request) throws Exception {
        // Check if the user's identity already exists in the wallet
        if (wallet.get(username) != null) {
            log.info("User {} is already enrolled and exists in the wallet.", username);
            return; // Skip registration and enrollment
        }

        HFCAClient caClient = setupHFCAClient(); // Initialize the CA client

        // Create a registration request
        RegistrationRequest regRequest = new RegistrationRequest(username);
        regRequest.setAffiliation(request.getAffiliation()); // Set user's organization affiliation
        regRequest.setType(request.getType());               // Set the user type (e.g., client)
        regRequest.setSecret(request.getSecret());           // Set the secret for enrollment
        regRequest.setMaxEnrollments(-1);                    // Allow unlimited enrollments

        // Register the user using the admin identity
        org.hyperledger.fabric.sdk.User adminUser = getAdminUser(); // Get admin credentials
        try {
            caClient.register(regRequest, adminUser);
            log.info("User {} registered successfully.", username);
        } catch (Exception e) {
            log.warn("User {} might already be registered: {}", username, e.getMessage());
        }

        // Enroll the user to get a certificate and key pair
        Enrollment enrollment = caClient.enroll(username, request.getSecret(), new EnrollmentRequest());
        var certificate = Identities.readX509Certificate(enrollment.getCert());
        var userIdentity = Identities.newX509Identity("Org1MSP", certificate, enrollment.getKey());

        // Store the identity in the wallet
        wallet.put(username, userIdentity);
        log.info("User {} enrolled and added to the wallet.", username);
    }
}
