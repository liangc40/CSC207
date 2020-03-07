package edu.toronto.group0162.service;

import edu.toronto.group0162.dao.CardDao;
import edu.toronto.group0162.dao.UserDao;
import edu.toronto.group0162.entity.Card;
import edu.toronto.group0162.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
public class CardService {

    @Getter
    private final CardDao cardDao;
    private final UserDao userDao;

    public void deleteCard(Card card) {
        if (this.cardDao.get(card.getCid()) != null) {
            Card cardDelete = this.cardDao.get(card.getCid());
            this.cardDao.delete(cardDelete);
        }
    }

    public Card addCard(int uid, Card card) {

        final User user = this.userDao.get(uid);
        if (null == user) {
            return null;
        }
        //绑定用户
        card.setUid(uid);
        return this.cardDao.save(card);
    }

    public Card addBalance(Card card, double deposit){
        card.setBalance(card.getBalance()+deposit);
        return this.cardDao.update(card);
    }

    public String printCardInfo(int cid){
        if (this.cardDao.get(cid) != null) {
            Card card = this.cardDao.get(cid);
            Date date = new Date(card.getCreateAt()*1000);
            return " Card "+card.getCid()+"\n"+
                    " balance: "+card.getBalance()+"\n"+
                    " Active Status: "+card.isActive()+"\n"+
                    " Delete? " +card.isDeleted() +"\n"+
                    " Created at: "+date;
        }
        else return "No Card Founded";
    }

}
