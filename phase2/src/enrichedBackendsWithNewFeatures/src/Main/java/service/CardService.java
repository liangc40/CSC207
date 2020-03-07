package service;

import dao.CardMoneyManagerDao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import manager.CardMoneyManager;
import manager.PastTripManager;
import dao.CardDao;
import dao.UserDao;
import dao.TransitPassDao;
import entity.Card;
import entity.TransitPass;
import entity.User;
import java.util.Iterator;

@AllArgsConstructor
public class CardService {
    @Getter
    private final CardDao cardDao;
    private final CardMoneyManagerDao cardMoneyManagerDao;


    public Card createNewCard(User user, CardMoneyManager cardMoneyManager, PastTripManager pastTripManager) {
        Card card = new Card();
        card.setUserID(user.getUserID());
        card.setActive(true);
        card.setCardMoneyManagerID(cardMoneyManager.getCardMoneyManagerID());
        card.setPastTripManagerID(pastTripManager.getPastTripManagerID());
        Card savedCard = cardDao.save(card);
        cardMoneyManager.setCardID(savedCard.getCardID());
        cardMoneyManagerDao.update(cardMoneyManager);
        return  savedCard;
    }
}
