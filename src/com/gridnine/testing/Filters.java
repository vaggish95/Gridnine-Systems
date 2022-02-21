package com.gridnine.testing;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Predicate;


public class Filters {

    public static void mainFilter( List <Flight> listOfFlights) {

        List <Flight> check1 = departedBeforeNow(listOfFlights);
        List <Flight> check2 = arrivedBeforeDeparture(check1);
        List <Flight> check3 = moreThanTwoHoursGrounded(check2);

        System.out.println();

        if (check3.size() != 0){
            System.out.println("Итоговый набор с учетом исключенных перелетов: ");
            for (Flight f : check3 ){
            System.out.println(f.toString());
            }

        } else
            System.out.println("НЕТ ПОЛЕТОВ ДЛЯ ВЫВОДА");
    }


    private static List <Flight> departedBeforeNow(List <Flight> listOfFlights) {

        Predicate <Flight> departedBeforeFilter = x -> {
            boolean checkPassed = true;
            for (Segment segment : x.getSegments()) {

                if (segment.getDepartureDate().isBefore(LocalDateTime.now())) {
                    checkPassed = false;
                    System.out.println("Полет исключен из списка фильтрации. Причина :\"вылет до текущего момента\". Данные о полете: " + x);
                }
            }
            return checkPassed;
        };

        List <Flight> filteredFlights = listOfFlights.stream().filter(departedBeforeFilter).toList();
        System.out.println();

        return filteredFlights;

    }


    private static List <Flight> arrivedBeforeDeparture (List <Flight> listOfFlights) {
        Predicate <Flight> arrivedBeforeDepartureFilter = x -> {
            boolean checkPassed = true;
            for (Segment segment : x.getSegments()) {

                if (segment.getArrivalDate().isBefore(segment.getDepartureDate())){
                    checkPassed = false;
                    System.out.println("Полет исключен из списка фильтрации. Причина :\"дата прилета сегмента раньше даты вылета\". Данные о полете: " + x);
                }
            }
            return checkPassed;
        };

        List <Flight> filteredFlights  = listOfFlights.stream().filter(arrivedBeforeDepartureFilter).toList();
        System.out.println();

        return filteredFlights ;
    }






    private static List <Flight> moreThanTwoHoursGrounded(List <Flight> listOfFlights) {

        Predicate <Flight> moreThanTwoHoursGroundedFilter = x -> {
            boolean flag = true;
            int timeCount = 0;
            for (int y = 0; y < x.getSegments().size() - 1 ; y ++) {
                LocalDateTime arrivalDate =x.getSegments().get(y).getArrivalDate();
                LocalDateTime departureDate =x.getSegments().get(y + 1).getDepartureDate();
                timeCount += ChronoUnit.MINUTES.between(arrivalDate,departureDate);
            }

            if (timeCount > 120) {
                System.out.println("Полет исключен из списка фильтрации. Причина :\"общее время на земле превышает 2 часа\". Фактическое время на земле составляет: " + timeCount + " минут. Данные о полете: " + x);
                flag = false;
            }

        return flag;

        };

        List<Flight> flights = listOfFlights.stream().filter(moreThanTwoHoursGroundedFilter).toList();
        System.out.println();

        return flights;
        }

}
