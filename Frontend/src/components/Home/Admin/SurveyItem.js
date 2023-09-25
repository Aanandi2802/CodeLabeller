import React from "react";
import "../Admin/SurveyItem.css";

/**
 *
 * @param {*} param0 Survey is get from the Admin View page.
 * @returns Renders the survey Information on the View all survey Page.
 */
const SurveyItem = ({ survey }) => {
  return (
    <>
      <h3>
        <span className="survey-name">
          {survey.surveyName}&nbsp;&nbsp;&nbsp;
        </span>

        <span className="survey-language">{" " + survey.surveyLanguage}</span>
      </h3>
      <p></p>
    </>
  );
};

export default SurveyItem;
