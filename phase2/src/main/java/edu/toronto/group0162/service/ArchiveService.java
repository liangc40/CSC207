package edu.toronto.group0162.service;

import edu.toronto.group0162.dao.TripSegmentDao;
import edu.toronto.group0162.entity.GeneralTrip;
import edu.toronto.group0162.dao.GeneralTripDao;
import edu.toronto.group0162.entity.TripSegment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class ArchiveService {

    @Getter
  private GeneralTripDao generalTripDao;
  private TripSegmentDao tripSegmentDao;

  public void archiveGeneralTrips(int year) {
    List<GeneralTrip> listGeneralTrips = this.generalTripDao.getBeforeYear(year);
    exportGeneralTrip("generalTripsArchive.csv",listGeneralTrips);
          Iterator generalTripsList = listGeneralTrips.iterator();
    while(generalTripsList.hasNext()) {
      GeneralTrip generalTripGet = (GeneralTrip) generalTripsList.next();
      this.generalTripDao.delete(generalTripGet.getGtid());
    }
  }

    public void archiveTripSegments(int year) {
        List<TripSegment> listTripSegments = this.tripSegmentDao.getBeforeYear(year);
        exportTripSegment("tripSegmentsArchive.csv",listTripSegments);
        Iterator tripSegmentsList = listTripSegments.iterator();
        while(tripSegmentsList.hasNext()) {
            TripSegment tripSegmentGet = (TripSegment) tripSegmentsList.next();
            this.tripSegmentDao.delete(tripSegmentGet.getTsid());
        }
    }

    private static final String FILE_HEADER_GENERAL_TRIP = "gtid,uid,starttime,finished";
    private static final String FILE_HEADER_TRIP_SEGMENT = "tsid,uid,gtid,cid,starttime,endtime,start,stop,transit_line,fare,cap";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String COMMA_DELIMITER = ",";

  public void exportGeneralTrip(String fileName, List<GeneralTrip> generalTrips) {

    try (FileWriter fileWriter = new FileWriter(fileName)) {
      // new file

      // Write the CSV file header
      fileWriter.append(FILE_HEADER_GENERAL_TRIP.toString());

      // Add a new line separator after the header
      fileWriter.append(NEW_LINE_SEPARATOR);

      // Write a new GeneralTrip object list to the CSV file
      for (GeneralTrip generalTrip : generalTrips) {
        fileWriter.append(String.valueOf(generalTrip.getGtid()));
        fileWriter.append(COMMA_DELIMITER);
        fileWriter.append(String.valueOf(generalTrip.getUid()));
        fileWriter.append(COMMA_DELIMITER);
        fileWriter.append(generalTrip.getStartTime());
        fileWriter.append(COMMA_DELIMITER);
        fileWriter.append(String.valueOf(generalTrip.isFinished()));
        fileWriter.append(NEW_LINE_SEPARATOR);
      }

      // System.out.println("CSV file was created successfully !!!");
      log.info("CSV file was created successfully");

    } catch (Exception e) {
      log.error("Error in CsvFileWriter");
      e.printStackTrace();
    }
  }

    public void exportTripSegment(String fileName, List<TripSegment> listTripSegments) {

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            // new file

            // Write the CSV file header
            fileWriter.append(FILE_HEADER_TRIP_SEGMENT.toString());

            // Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            // Write a new TripSegment object list to the CSV file
            for (TripSegment tripSegment : listTripSegments) {
                fileWriter.append(String.valueOf(tripSegment.getTsid()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(tripSegment.getUid()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(tripSegment.getGtid()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(tripSegment.getCid()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(tripSegment.getEnterTime());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(tripSegment.getExitTime());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(tripSegment.getEntrance());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(tripSegment.getExit());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(tripSegment.getTransitLine());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(tripSegment.getFare()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(tripSegment.getCap()));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            // System.out.println("CSV file was created successfully !!!");
            log.info("CSV file was created successfully");

        } catch (Exception e) {
            log.error("Error in CsvFileWriter !!!");
            e.printStackTrace();
        }
    }
}
