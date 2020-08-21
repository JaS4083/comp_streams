import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {



    public static void main(String[] args) throws IOException {
        System.out.println(new Main().listOfUsers());
    }

    private List<String> listOfUsers() throws IOException {

        final List<String> startStrings = Files.readAllLines(Paths.get("data/tag_read_start.log"));
        final List<String> finishStrings = Files.readAllLines(Paths.get("data/tag_reads_finish.log"));

        //getting mapped data from finish file <Tag, Time>
        final Map<String, Long> finishMap = startStrings.stream()
                .collect(Collectors.groupingBy(x -> x.substring(4, 16), (Collectors.maxBy(Comparator.comparing(x -> (x.substring(20, 32)))))))
                .entrySet().stream().filter(k -> k.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, x -> Long.parseLong(x.getValue().get().substring(20, 32))));


        //getting mapped data from start file <Tag, Time>
        final Map<String, Long> startingMap = finishStrings.stream()
                .collect(Collectors.groupingBy(x -> x.substring(4, 16), (Collectors.minBy(Comparator.comparing(x -> x.substring(20, 32))))))
                .entrySet().stream().filter(k -> k.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, x -> Long.parseLong(x.getValue().get().substring(20, 32))));

        //merging two maps
        final Map<String, Long> resultMap = Stream.of(finishMap, startingMap)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (mapVal, map2Val) -> (mapVal - map2Val)
                ));

        //getting new LinkedHashMap sorted by values
        final LinkedHashMap<String, Long> collected = resultMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));


        //getting result from sorted map, - first 10 tags (with the lowest numbers of time
        final List<String> result = collected.keySet().stream().limit(10).collect(Collectors.toList());

        return result;
    }
}
