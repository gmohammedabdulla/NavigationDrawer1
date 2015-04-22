package com.example.android.navigationdrawerexample;

/**
 * Created by gabdulla on 4/22/2015.
 */
public class transacitonListItem {
   public String sender,receiver,reason,amount,time;
  // public  float amount;

    public transacitonListItem(String sender,String receiver,String amount){
        this.amount=amount;
        this.receiver=receiver;
        this.sender=sender;
    }
}
