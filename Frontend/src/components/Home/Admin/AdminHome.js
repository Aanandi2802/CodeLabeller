import React from "react";
import "./AdminHome.css";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../Security/AuthContext";
import Header from "./AdminHeader";

/**
 *
 * @returns This component render the home page for Admin.
 */
const AdminHome = () => {
  const navigate = useNavigate();
  const authContext = useAuth();

  /**
   * handleClickButton1() handles the  button to navigate to page of creating survey.
   */
  const handleClickButton1 = () => {
    navigate("/admin/home/createSurvey");
  };

  /**
   * handleClickButton2() handles the  button to navigate to page of viewing surveys.
   */
  const handleClickButton2 = () => {
    navigate("/admin/home/viewSurveys");
  };

  return (
    <div>
      <div>
        <Header title="Admin Home Page" />
      </div>
      <div className="container">
        <div className="centered-element">
          <button className="button button-1" onClick={handleClickButton1}>
            Create Survey
          </button>
          <button className="button button-2" onClick={handleClickButton2}>
            View Surveys
          </button>
        </div>
      </div>
    </div>
  );
};

export default AdminHome;
