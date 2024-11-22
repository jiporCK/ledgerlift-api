package com.example.ledgerlift.features.receipt;

import com.example.ledgerlift.domain.Receipt;
import com.example.ledgerlift.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long>{

    List<Receipt> findAllByUser(User user);

}
