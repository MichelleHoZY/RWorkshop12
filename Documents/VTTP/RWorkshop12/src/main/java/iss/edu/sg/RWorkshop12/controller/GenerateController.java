package iss.edu.sg.RWorkshop12.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import iss.edu.sg.RWorkshop12.exception.RandomNumberException;
import iss.edu.sg.RWorkshop12.model.Generate;

@Controller
public class GenerateController {
    private Logger logger = LoggerFactory.getLogger(GenerateController.class);

    @GetMapping("/")
    public String showGenerateForm(Model model){
        Generate generate = new Generate();
        model.addAttribute("generateObj", generate);
        return "generatePage";
        // you're creating the root page, creating a model class Generate called "generateObj" that returns "generatePage.html"
        // because thymeleaf form requires you to have a class for {generateObj}
        // generatePage will direct the user to PostMapping(/generate) as the endpoint after submitting
    }

    @PostMapping("/generate")
    public String generateNumbers(@ModelAttribute Generate generate, Model model){
        try{
            logger.info("From the form " + generate.getNumberVal());
            // the number of random numbers you need to generate
            int numberRandomNumbers = generate.getNumberVal();
            // if the number entered exceeds the maximum number, throw random number exception error
            if(numberRandomNumbers > 3){
                throw new RandomNumberException();
            }

            // initialising all number images
            String[] imgNumbers = {
                "number1.png", "number2.png", "number3.png"
            };

            // create a list of string that holds the selected images from the randomly generated numbers
            List<String> selectedImg = new ArrayList<String>();
            // randomising the numbers
            Random randNum = new Random();
            // LinkedHashSet maintains a linked list of the entires in the set, in the order in which they were inserted.
            // This allows insertion-order iteration over the set.
            // When cycling through a LinkedHashSet using an iterator, the elements will be returned in the order in which they were inserted.
            Set<Integer> uniqueGeneratedRandNumSet = new LinkedHashSet<Integer>();
            // loop through every number generated from the Random class and place it into the hash LinkedHashSet
            while(uniqueGeneratedRandNumSet.size()<numberRandomNumbers){
                Integer resultOfRandNumbers = randNum.nextInt(generate.getNumberVal()+1);
                uniqueGeneratedRandNumSet.add(resultOfRandNumbers);
            }
            // once the above process is done, we have a list of randomly generated numbers
            // check that it is unique

            Iterator<Integer> it = uniqueGeneratedRandNumSet.iterator();
            Integer currentElem = null;
            while(it.hasNext()){ // while it list has another token
                currentElem = it.next(); // current element = next token in it list
                logger.info("currentElem > " + currentElem); // logging the current element
                selectedImg.add(imgNumbers[currentElem.intValue()]); // add the image number of the current element's value to the selectedImg list
            }

            model.addAttribute("randNumsResult", selectedImg.toArray()); // all the numbers in selectedImg named "randNumsResult" as a model attribute
            model.addAttribute("numInputByUser", numberRandomNumbers); // the inputted random number by user as "numInputByUser"
        } catch(RandomNumberException e){
            model.addAttribute("errorMessage", "More than 3 entered!");
            return "error";
        }
        return "result";
    }
    
}
