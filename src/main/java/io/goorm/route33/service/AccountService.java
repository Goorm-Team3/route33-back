package io.goorm.route33.service;

import io.goorm.route33.model.Account;
import io.goorm.route33.model.code.OnOffStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final SecureRandom random = new SecureRandom();

    /**
     * 새로운 계좌를 생성한다.
     *
     * @param userId
     */
    public void createAccount(Long userId) {
        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .balance(0)
                .createTime(LocalDateTime.now())
                .activeYn(OnOffStatus.ON)
                .userId(userId).build();

        accountRepository.save(account);
    }

    private String generateAccountNumber() {
        StringBuilder sb = new StringBuilder(12);
        for (int i=0; i<12; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}
