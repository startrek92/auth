package tmp;

import java.util.HashMap;
import java.util.Map;

class Solution {

    private Map<Integer, Long> mp;
    public Solution(long[] balance) {
        this.mp = new HashMap<>();
        for(int i=0; i<balance.length; ++i) {
            this.mp.put(i+1, balance[i]);
        }

    }

    public boolean transfer(int account1, int account2, long money) {
        if(!this.mp.containsKey(account1) || !this.mp.containsKey(account2)) {
            return false;
        }

        if (this.mp.get(account1) < money) {
            return false;
        }

        this.mp.put(account1, mp.get(account1)-money);
        this.mp.put(account2, mp.get(account2) + money);

        return true;

    }

    public boolean deposit(int account, long money) {
        if(!this.mp.containsKey(account)) {
            return false;
        }

        this.mp.put(account, mp.get(account) + money);
        return true;
    }

    public boolean withdraw(int account, long money) {
        if(!this.mp.containsKey(account)) {
            return false;
        }

        if(this.mp.get(account) < money) {
            return false;
        }

        this.mp.put(account, mp.get(account) - money);
        return true;
    }
}

/**
 * Your Bank object will be instantiated and called as such:
 * Bank obj = new Bank(balance);
 * boolean param_1 = obj.transfer(account1,account2,money);
 * boolean param_2 = obj.deposit(account,money);
 * boolean param_3 = obj.withdraw(account,money);
 */