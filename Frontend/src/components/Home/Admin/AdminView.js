import React from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./AdminView.css";
import admin from "./AdminModel";
import { useEffect } from "react";
import { useState } from "react";
import SurveyItem from "./SurveyItem";
import Header from "./AdminHeader";
import { Button } from "react-bootstrap";

/**
 * All survey pages where admin will see all surveys that is uploaded & Annotated.
 * @returns survey page
 */
const AdminView = () => {
  const changePage = useNavigate();
  const [surveys, setSurveys] = useState(null);

  const admin_name = admin.name;
  window.localStorage.setItem("adminName", admin_name);

  /**
   * Re-rendering by calling the GET API from backend to collect all surveys.
   */
  useEffect(() => {
    axios
      .get("/admin/" + admin_name + "/survey/all/", {
        headers: {
          Authorization: window.localStorage.getItem("jwtToken"),
        },
      })
      .then((response) => {
        setSurveys(response.data);
      })
      .then(() => {
        changePage("/admin/home/viewSurveys");
      })
      .catch((error) => {
        console.error("Error loading surveys:", error);
      });
  }, []);

  if (!surveys) {
    return (
      <div className="loading-container">
        <div className="loading-spinner">
          <div className="loading-bar"></div>
          <div className="loading-bar"></div>
          <div className="loading-bar"></div>
          <div className="loading-bar"></div>
        </div>
        <div className="loading-text">Loading...</div>
      </div>
    );
  }

  /**
   *
   * @param {*} surveyID To View the survey which is clicked by admin.
   */
  const handleViewUploaded = (surveyID) => {
    window.localStorage.setItem("surveyID", surveyID);
    window.localStorage.setItem("adminName", admin_name);
    changePage(`/admin/home/viewSurveys/${surveyID}`);
  };

  /**
   * Handles the Viewing of Annotated survey page.
   * @param {*} surveyID To view this Annotated survey.
   */
  const handleViewAnnotated = (surveyID) => {
    window.localStorage.setItem("surveyID", surveyID);
    window.localStorage.setItem("adminName", admin_name);
    changePage(`/admin/home/viewAnnotatedSurveys/${surveyID}`);
  };

  return (
    <div>
      <Header title="List of Surveys" />
      <div className="surveys-container">
        <ul className="surveys-list">
          {surveys.map((survey) => (
            <li key={survey.surveyID} className="survey-item">
              <div className="item">
                <div className="item_name">
                  <SurveyItem survey={survey} />
                </div>
                <div className="item_button">
                  <Button
                    className="button1"
                    onClick={() => handleViewUploaded(survey.surveyID)}
                  >
                    View
                  </Button>
                  <Button
                    className="button2"
                    onClick={() => handleViewAnnotated(survey.surveyID)}
                  >
                    Responses
                  </Button>
                </div>
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default AdminView;
