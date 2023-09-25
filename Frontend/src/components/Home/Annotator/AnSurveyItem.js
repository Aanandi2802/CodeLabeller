import React from "react";

/**
 *
 * @param {*} param0 survey which is to be shown
 * @returns Render the information of particular survey
 */
const SurveyItem = ({ survey }) => {
  return (
    <div>
      <div>
        <h3>
          <span className="survey-name">
            {survey.surveyName}&nbsp;&nbsp;&nbsp;
          </span>
          <span className="survey-language">{survey.surveyLanguage}</span>
        </h3>
      </div>
    </div>
  );
};

export default SurveyItem;
