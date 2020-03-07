import java.util.Observer;
import java.util.Observable;
import java.text.DecimalFormat;

/**
 * Represents a daily report which implements the Observer interface when it wants to be informed of changes
 * in Observable Trip objects and it stores total revenue and admin costs for a date.
 *
 */

public class DailyReport implements Observer{
    /** String representing the date of a daily report */
    String date;
    /** Double representing total revenue within a daily report */
    private double dailyRevenue;
    /** Double representing total cost within a daily report */
    private double dailyCost;

    /**
     * Initializes a daily report.
     *
     * @param date String representing the date for a daily report
     *
     */
    DailyReport(String date) {
        this.date = date.split(" ")[0];
    }

    /**
     * Gets the date of a daily report.
     *
     * @return String representing the date for a daily report
     *
     */
    String getDate() {
        return date;
    }

    /**
     * Updates a daily report's total revenue and admin cost
     *
     * @param observable representing the observable Trip object
     * @param arg representing an argument passed to Trip's methods of getting charge and admin cost
     *
     */
    public void update(Observable observable, Object arg) {
        //update total revenue
        if (arg.equals("update total revenue")) {
            dailyRevenue += (((GeneralTrip) observable).getChargeAmount());
            //update total admin cost
        } else if (arg.equals("update total cost")){
            dailyCost += ((GeneralTrip) observable).getAdminCost();
        }
        observable.deleteObserver(this);
    }

    /**
     * Gets total revenue of a daily report.
     *
     * @return double representing total revenue for a daily report
     *
     */
    double getDailyRevenue() {
        return dailyRevenue;
    }

    /**
     * Gets total admin costs of a daily report.
     *
     * @return double representing total admin costs for a daily report
     *
     */
    double getDailyCost() {
        return dailyCost;
    }

    /**
     * Returns a string representation of DailyReport Object
     *
     * @return String representing DailyReport Object
     *
     */
    @Override
    public String toString() {
        Double formattedDailyCost = convertDecimalFormat(dailyCost);
        Double formattedDailyRevenue = convertDecimalFormat(dailyRevenue);
        return ("date " + date + ": total cost is " + formattedDailyCost + " total revenue is " + formattedDailyRevenue);
    }

    /**
     * Converts a double representation an input with double type
     *
     * @return Double representing the converted double object
     *
     */
    public double convertDecimalFormat(double inputDouble) {
        DecimalFormat newFormat = new DecimalFormat("#.#");
        return Double.valueOf(newFormat.format(inputDouble));
    }

}
