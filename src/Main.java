import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final int TAG_STARTS_AT = 4;
    private static final int TAG_ENDS_AT = 16;
    private static final int TIMESTAMP_STARTS_AT = 20;
    private static final int TIMESTAMP_ENDS_AT = 32;



    private static Function<String, String> tagSubstringLogic() {
        return x -> x.substring(TAG_STARTS_AT, TAG_ENDS_AT);
    }

    private static Function<String, Long> unixTimeParsingLogic() {
        return x -> Long.parseLong(x.substring(TIMESTAMP_STARTS_AT, TIMESTAMP_ENDS_AT));
    }


    public static void main(String[] args) throws IOException {

        Map<String, Long> start = Files.readAllLines(Paths.get("data/tag_read_start.log"))
                //getting mapped data from start file <Tag, Time> (when key conflict, take the first one)
                .stream().collect(Collectors.toMap(tagSubstringLogic(), unixTimeParsingLogic(), (o1, o2) -> o1));

        //getting mapped data from finish file <Tag, Time> (when key conflict, take the second one)
        Map<String, Long> finish = Files.readAllLines(Paths.get("data/tag_reads_finish.log"))
                .stream().collect(Collectors.toMap(tagSubstringLogic(), unixTimeParsingLogic(), (o1, o2) -> o2));

        //combining two streams
        Stream.of(finish, start).flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (map1Val, map2Val) -> (map1Val - map2Val)))
                .entrySet().stream().sorted(Map.Entry.comparingByValue())
                .limit(10).forEach(x -> System.out.println(
                        "The participant with tag " + x.getKey() +
                                ", took " + TimeUnit.MILLISECONDS.toSeconds(x.getValue()) +
                                " seconds, (" + x.getValue() + " milliseconds) to complete the test."));
    }
}
