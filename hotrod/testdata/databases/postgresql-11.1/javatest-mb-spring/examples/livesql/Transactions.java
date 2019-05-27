package examples.livesql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import hotrod.test.generation.Account;
import hotrod.test.generation.primitives.AccountDAO;

@Component("transactions")
public class Transactions {

  @Autowired
  private AccountDAO dao;

  @Transactional
  public void transfer(final Integer fromAccountId, final Integer toAccountId, final Integer amount) {

    System.out.println("--- Transfer starting...");

    Account from = dao.selectByPK(fromAccountId);
    Account to = dao.selectByPK(toAccountId);

    System.out
        .println("--- Transferring $" + amount + " from account " + from.getName() + " to account " + to.getName());

    to.setCurrentBalance(to.getCurrentBalance() + amount);
    dao.update(to);
    System.out
        .println("+++ Credited $" + amount + " to account " + to.getName() + ": balance $" + to.getCurrentBalance());

    from.setCurrentBalance(from.getCurrentBalance() - amount);
    if (from.getCurrentBalance() < 0) {
      throw new RuntimeException("Insufficient funds");
    }
    dao.update(from);

    System.out.println("Transferred: new balances are: $" + from.getCurrentBalance() + " - $" + to.getCurrentBalance());

  }

}
