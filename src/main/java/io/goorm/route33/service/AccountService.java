package io.goorm.route33.service;

import io.goorm.route33.exception.CustomException;
import io.goorm.route33.model.Account;
import io.goorm.route33.model.code.OnOffStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Slf4j
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

    /**
     * 계좌에 금액을 입금하고 잔액을 반환한다.
     *
     * @param userId
     * @param amount
     * @return
     */
    public int deposit(Long userId, int amount) {
        Account account = getAccountWithUserId(userId);
        log.info("입금 전 잔액: {}", account.getBalance());
        account.depositBalance(amount);

        return account.getBalance();
    }

    private Account getAccountWithUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException("회원 번호 '" + userId + "' 에 대한 계좌를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));
    }

    private String generateAccountNumber() {
        StringBuilder sb = new StringBuilder(12);
        for (int i=0; i<12; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}
