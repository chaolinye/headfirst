package org.freedom.testflight;

import org.apache.commons.lang3.StringUtils;
import org.freedom.testflight.utils.POIUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestFlightMain {

    public static void main(String[] args) throws IOException {
        String fileName = "/Users/chaolinye/Downloads/emails.xlsx";
        List<String[]> accountPasswords = POIUtils.readExcel(fileName);
        System.out.println("the number of account:" + accountPasswords.size());
        TestFlightExtractor testFlightExtractor = new TestFlightExtractor();
        List<String> testFlightLinks = new ArrayList<String>();
        for (String[] accountPassword : accountPasswords) {
            String account = accountPassword[0].trim();
            String password = accountPassword[1].trim();
            System.out.println(account + "---" + password);
            String testFlightLink = testFlightExtractor.extractLastTestFlightLink(account, password);
            if (StringUtils.isBlank(testFlightLink)) {
                System.out.println("account=" + account + ",password=" + password + " has not new testFlightlink");
            } else {
                System.out.println("link=" + testFlightLink);
                testFlightLinks.add(testFlightLink);
            }
        }
        System.out.println("the number of testFlightLinks:" + testFlightLinks.size());
        System.out.println(testFlightLinks);
        if(testFlightLinks.size()==0){
            return;
        }
        String outputFileName = "/Users/chaolinye/Downloads/testflight-beta.xlsx";
        List<String[]> testFlightLinksToOutput = testFlightLinks.stream().map(x -> new String[]{x}).collect(Collectors.toList());
        POIUtils.writeExcel(outputFileName, testFlightLinksToOutput);

    }
}
