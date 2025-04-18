package io.goorm.route33.service;

import io.goorm.route33.exception.CustomException;
import io.goorm.route33.model.Account;
import io.goorm.route33.model.User;
import io.goorm.route33.model.code.OnOffStatus;
import io.goorm.route33.model.dto.AccountInfoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
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
     * 계좌 정보를 반환한다.
     *
     * @param userId
     * @return
     */
    public AccountInfoResponseDto getAccountInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("존재하지 않는 회원입니다.", HttpStatus.BAD_REQUEST));

        Account account = getAccountWithUserId(userId);

        return AccountInfoResponseDto.builder()
                .username(user.getUsername())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance()).build();
    }

    /**
     * 계좌에 금액을 입금하고 잔액을 반환한다.
     *
     * @param userId
     * @param amount
     * @return
     */
    @Transactional
    public int deposit(Long userId, int amount) {
        Account account = getAccountWithUserId(userId);
        log.info("입금 전 잔액: {}", account.getBalance());

        account.depositBalance(amount);
        accountRepository.save(account);

        return account.getBalance();
    }

    /**
     * 계좌에서 금액을 출금하고 잔액을 반환한다.
     *
     * @param userId
     * @param amount
     * @return
     */
    @Transactional
    public int withdrawal(Long userId, int amount) {
        Account account = getAccountWithUserId(userId);
        log.info("출금 전 잔액: {}", account.getBalance());
        if (account.getBalance() < amount) {
            throw new CustomException("출금을 위한 잔액이 부족합니다, 현재 잔액: " + account.getBalance(), HttpStatus.BAD_REQUEST);
        }

        account.withdrawalBalance(amount);
        accountRepository.save(account);

        return account.getBalance();
    }

    /**
     * 다른 계좌로 송금한다.
     *
     * @param userId
     * @param targetAccountNumber
     * @param amount
     */
    @Transactional
    public void transfer(Long userId, String targetAccountNumber, int amount) {
        Account fromAccount = getAccountWithUserId(userId);
        if (fromAccount.getBalance() < amount) {
            throw new CustomException("송금을 위한 잔액이 부족합니다, 현재 잔액: " + fromAccount.getBalance(), HttpStatus.BAD_REQUEST);
        }
        Account toAccount = getAccountWithAccountNumber(targetAccountNumber);

        fromAccount.withdrawalBalance(amount);
        toAccount.depositBalance(amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    private Account getAccountWithUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException("회원 번호 '" + userId + "' 에 대한 계좌를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));
    }

    private Account getAccountWithAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new CustomException("계좌 번호 '" + accountNumber + "' 에 대한 계좌를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));
    }

    private String generateAccountNumber() {
        StringBuilder sb = new StringBuilder(12);
        for (int i=0; i<12; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}
