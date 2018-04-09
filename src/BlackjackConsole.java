import java.util.Scanner;
import java.util.Vector;


public class BlackjackConsole
{

    private static Scanner scanner = new Scanner(System.in);
    private int[] deck;
    private int currentPosition;


    public static void main(String[] args)
    {
        new BlackjackConsole().Run();
    }

    public void Run()
    {
        int money;
        int bet;
        boolean userWins;

        System.out.println();

        money = 100;

        while (true)
        {
            System.out.println("You have " + money + " dollars.");
            do
            {
                System.out.println("How many dollars do you want to bet?  (Enter 0 to end.)");
                bet = scanner.nextInt();
                if (bet < 0 || bet > money)
                {
                    System.out.println("Your answer must be between 0 and " + money + '.');
                }
            } while (bet < 0 || bet > money);
            if (bet == 0)
            {
                break;
            }
            userWins = playBlackjack();
            if (userWins)
            {
                money = money + bet;
            } else
            {
                money = money - bet;
            }
            System.out.println();
            if (money == 0)
            {
                System.out.println("Looks like you've are out of money!");
                break;
            }
        }

        System.out.println();
        System.out.println("You leave with $" + money + '.');

    }

    private boolean playBlackjack()
    {
        Vector dealerHand;
        Vector userHand;

        deck = new int[52];
        int cardCt = 0;
        for (int suit = 0; suit <= 3; suit++)
        {
            for (int value = 1; value <= 13; value++)
            {
                deck[cardCt] = value;
                cardCt++;
            }
        }
        currentPosition = 0;

        dealerHand = new Vector();
        userHand = new Vector();

        shuffle();

        dealerHand.addElement(dealCard());
        dealerHand.addElement(dealCard());
        userHand.addElement(dealCard());
        userHand.addElement(dealCard());


        if (value(dealerHand) == 21)
        {
            System.out.println("Dealer has the " + showCard(getCard(dealerHand, 0)) + " and the " + showCard(getCard(dealerHand, 1)) + ".");
            System.out.println("User has the " + showCard(getCard(userHand, 0)) + " and the " + showCard(getCard(userHand, 1)) + ".");
            System.out.println();
            System.out.println("Dealer has Blackjack.  Dealer wins.");
            return false;
        }

        if (value(userHand) == 21)
        {
            System.out.println("Dealer has the " + showCard(getCard(dealerHand, 0)) + " and the " + showCard(getCard(dealerHand, 1)) + ".");
            System.out.println("User has the " + showCard(getCard(userHand, 0)) + " and the " + showCard(getCard(userHand, 1)) + ".");
            System.out.println();
            System.out.println("You have Blackjack.  You win.");
            return true;
        }

        while (true)
        {
            System.out.println();
            System.out.println("Your cards are:");
            for (int i = 0; i < userHand.size(); i++)
            {
                System.out.println("    " + showCard(getCard(userHand, i)));
            }
            System.out.println("Your total is " + value(userHand));
            System.out.println();
            System.out.println("Dealer is showing the " + showCard(getCard(dealerHand, 0)));
            System.out.println();
            System.out.print("Hit (H) or Stand (S)? ");
            char userAction;
            do
            {
                userAction = Character.toUpperCase(scanner.next().charAt(0));
                if (userAction != 'H' && userAction != 'S')
                {
                    System.out.print("Please respond H or S:  ");
                }
            } while (userAction != 'H' && userAction != 'S');

            if (userAction == 'S')
            {
                break;
            } else
            {
                int newCard = dealCard();
                userHand.addElement(newCard);
                System.out.println();
                System.out.println("User hits.");
                System.out.println("Your card is the " + showCard(newCard));
                System.out.println("Your total is now " + value(userHand));
                if (value(userHand) > 21)
                {
                    System.out.println();
                    System.out.println("You busted by going over 21.  You lose.");
                    System.out.println("Dealer's other card was the " + showCard(getCard(dealerHand, 1)));
                    return false;
                }
            }

        }

        System.out.println();
        System.out.println("User stands.");
        System.out.println("Dealer's cards are");
        System.out.println("    " + showCard(getCard(dealerHand, 0)));
        System.out.println("    " + showCard(getCard(dealerHand, 1)));
        while (value(dealerHand) <= 16)
        {
            int newCard = dealCard();
            System.out.println("Dealer hits and gets the " + showCard(newCard));
            dealerHand.addElement(newCard);
        }
        System.out.println("Dealer's total is " + value(dealerHand));

        System.out.println();
        if (value(dealerHand) > 21)
        {
            System.out.println("Dealer busted by going over 21.  You win.");
            return true;
        } else
        {
            if (value(dealerHand) == value(userHand))
            {
                System.out.println("Dealer wins on a tie.  You lose.");
                return false;
            } else
            {
                if (value(dealerHand) > value(userHand))
                {
                    System.out.println("Dealer wins, " + value(dealerHand) + " points to " + value(userHand) + ".");
                    return false;
                } else
                {
                    System.out.println("You win, " + value(userHand) + " points to " + value(dealerHand) + ".");
                    return true;
                }
            }
        }

    }

    public int dealCard()
    {
        if (currentPosition == 52)
        {
            shuffle();
        }
        currentPosition++;
        return deck[currentPosition - 1];
    }

    public void shuffle()
    {
        for (int i = 51; i > 0; i--)
        {
            int rand = (int) (Math.random() * (i + 1));
            int temp = deck[i];
            deck[i] = deck[rand];
            deck[rand] = temp;
        }
        currentPosition = 0;
    }

    public int getCard(Vector hand, int position)
    {
        if (position >= 0 && position < hand.size())
        {
            return ((Integer)hand.elementAt(position)).intValue();
        } else
        {
            return 0;
        }
    }

    public int value(Vector hand)
    {

        int val;
        boolean ace;
        int cards;

        val = 0;
        ace = false;
        cards = hand.size();

        for (int i = 0; i < cards; i++)
        {
            int card;
            int cardVal;
            card = getCard(hand, i);
            cardVal = getCardValue(card);
            if (cardVal > 10)
            {
                cardVal = 10;
            }
            if (cardVal == 1)
            {
                ace = true;
            }
            val = val + cardVal;
        }

        if (ace == true && val + 10 <= 21)
        {
            val = val + 10;
        }

        return val;

    }
    public int getCardValue(int card)
    {
        int result = card;
        switch (card)
        {
            case 11:
            case 12:
            case 13:
                result =  10;
        }
        return result;
    }
    public String showCard(int card)
    {
        switch (card)
        {
            case 1:
                return "Ace";
            case 2:
                return "2";
            case 3:
                return "3";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "7";
            case 8:
                return "8";
            case 9:
                return "9";
            case 10:
                return "10";
            case 11:
                return "Jack";
            case 12:
                return "Queen";
            case 13:
                return "King";
            default:
                return "??";
        }
    }
}