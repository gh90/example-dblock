package com.gh90.example.dblock.payment;

import com.gh90.example.dblock.account.Account;
import com.gh90.example.dblock.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final AccountRepository accountRepository;

    @Transactional
    public boolean pay(Long accountId, int price){
        System.out.println("select start");
        Account account = accountRepository.findByIdForUpdate(accountId);
        System.out.println("select end");
        if(account.canPay(price)){
            account.minusBalance(price);
            accountRepository.save(account);
            System.out.println("성공 : " + account+", 요청 price"+ price);
            return true;
        }else{
            System.out.println("실패 : " + account+", 요청 price"+ price);
            return false;
        }
    }
}
