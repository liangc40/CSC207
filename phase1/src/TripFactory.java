/** A Trip factory which could generate new trips. */
class TripFactory {
    /**
     * Create a new general trip which could be packaged to a new bus trip
     * a new subway trip.
     *
     * @param tripType a string representing the type of trip that could be created.
     * @return  a general trip which could be either a bus trip or a subway trip
     */
    static GeneralTrip createNewTrip(String tripType) {
        if (tripType == null) {
            return null;
        }
        // If the new trip type is "Bus", then create a new bus trip
        if (tripType.equalsIgnoreCase("Bus")) {
            return new BusTrip();
            // If the new trip type is "Subway". then create a new subway trip
        } else if (tripType.equalsIgnoreCase("Subway")) {
            return new SubwayTrip();
        }
        return null;
    }
}
