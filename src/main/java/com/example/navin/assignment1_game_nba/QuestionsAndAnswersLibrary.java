package com.example.navin.assignment1_game_nba;

/**
 * the class for storing all the questions and answer choices in different arrays.
 */


public class QuestionsAndAnswersLibrary {

    //array of string for storing the questions.
    private String questions[] = {
            "What is the force of an object with a mass of 10kg and an acceleration of 3m/s?",
            "A golf cart is driven at its top speed of 27 km/h for 10 minutes. How far did the golf cart travel?",
            "What is the speed of a car that is traveling 10 miles every 5 minutes?",
            "A hairdryer transfers 4800 Joules of energy in 1 minute. What is the power rating of the hairdryer?",
            "Calculate the amount of carbon atoms in 6.0 g of carbon. (Ar of C = 12)",
            "What is the mass of an object with a force of 81N and an acceleration of 3m/s?",
            "Iain walked from his parents' farm into town at a steady speed of 5km/h. The journey took 3 hours. How far did Iain walk?",
            "Kelly runs from 4.50pm until 5.20pm at an average speed of 7km/h. How far did she go?",
            "Calculate the force needed to accelerate a 20 kg cheetah at 10 m/s².",
            "What is the overall stopping distance of a lorry that has a thinking distance of 12m and a braking distance of 30m?"

    };

    //two-dimensional array for storing the multiple answer choices for each question.
    private String answerChoices[][] = {
            {"13N", "30N", "7N"},
            {"37 Km", "2.7 Km", "270 Km"},
            {"2 m/m", "15 m/m", "50 m/m"},
            {"60W", "70W", "80W"},
            {"0.4 mol", "0.5 mol", "0.6 mol"},
            {"48 Kg", "84 Kg", "27 kg"},
            {"15 km", "8km", "2km"},
            {"0.5 hr", "1 hr", "1.5 hr"},
            {"30N", "200N", "10N"},
            {"18m", "-18m", "42m"},
    };

    //an array of string for storing the correct answers for the above questions.
    private String correctAnswers[] = {"30N", "270 Km", "2 m/m", "80W", "0.5 mol", "27 kg", "15 km", "0.5 hr", "200N", "42m"};

    public String getQuestion(int questionPos) {
        String question = questions[questionPos];
        return question;
    }

    public String getAnswerChoice1(int answerChoicePos) {
        String answer = answerChoices[answerChoicePos][0];
        return answer;
    }

    public String getAnswerChoice2(int answerChoicePos) {
        String answer = answerChoices[answerChoicePos][1];
        return answer;
    }

    public String getAnswerChoice3(int answerChoicePos) {
        String answer = answerChoices[answerChoicePos][2];
        return answer;
    }

    public String getCorrectAnswer(int correctAnswerPos) {
        String answer = correctAnswers[correctAnswerPos];
        return answer;
    }

    public int getNoOfQuestions() {
        return questions.length;
    }
}