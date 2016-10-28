package org.faboo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        Pattern pattern = Pattern.compile("flag-(?<flag>\\D{2})\\.svg");
        StringBuilder xml = new StringBuilder("<list>\n");

        Map<String,List<String>> m = Files.list(Paths.get(args[0]))
                .map((Path::toString))
                .map(name -> {
                    Matcher matcher =pattern.matcher(name);
                    matcher.find();
                    return matcher.group("flag");
                })
                .sorted()
                .collect(
                        Collectors
                                .groupingBy(flag -> flag.substring(0,1),
                                        Collectors.mapping(flag -> flag, Collectors.toList())));

        System.out.println(m);
        m.keySet().forEach(letter -> {
            xml.append("<" + letter.toUpperCase() + ">\n");
            xml.append(m.get(letter).stream().map(flag -> String.format("<entry>\n" +
                    "                <id>%1$S</id>\n" +
                    "                <title>COUNTRY.%1$S</title>\n" +
                    "                <img>assets/images/flags/flag-%1$s.svg</img>\n" +
                    "            </entry>\n", flag))
                    .collect(Collectors.joining()));
            xml.append("</" + letter.toUpperCase() + ">\n");
        });
        xml.append("</list>");
        System.out.println(xml);

        /*        .map(flag -> String.format("<entry>\n" +
                        "                <id>%1$S</id>\n" +
                        "                <title>COUNTRY.%1$S</title>\n" +
                        "                <img>assets/images/flags/flag-%1$s.svg</img>\n" +
                        "            </entry>", flag))
                .collect(Collectors.joining())
        */
        ;


    }

}
