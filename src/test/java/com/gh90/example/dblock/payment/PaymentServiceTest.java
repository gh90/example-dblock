package com.gh90.example.dblock.payment;

import com.gh90.example.dblock.account.Account;
import com.gh90.example.dblock.account.AccountRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootTest
class PaymentServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentService paymentService;

    @Test
    void 동시성테스트() throws InterruptedException {
        Account account = new Account();
        account.plusBalance(2000);
        accountRepository.save(account);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch (2);

        AtomicBoolean result1 = new AtomicBoolean(true);
        AtomicBoolean result2 = new AtomicBoolean(true);

        executorService.execute(() -> {
            result1.set(paymentService.pay(Long.valueOf(1), 2000));
            latch.countDown();
        });
        executorService.execute(() -> {
            result2.set(paymentService.pay(Long.valueOf(1), 1000));
            latch.countDown();
        });
        latch.await();
        Account account2 = accountRepository.findById(Long.valueOf(1)).get();

        SoftAssertions assertions =new SoftAssertions();
        assertions.assertThat(account2.getBalance()).as("첫번째 결제 결과").isEqualTo(result1.get()?0:1000);
        assertions.assertThat(account2.getBalance()).as("두번째 결제 결과").isEqualTo(result2.get()?1000:0);
        assertions.assertThat(result1.get()).as("두결과는 항상 달라야함")
                .isNotEqualTo(result2.get());
        assertions.assertAll();
    }
}