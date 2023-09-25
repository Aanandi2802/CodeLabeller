import { useState } from "react";
import "./App.css";
import Login from "./components/Login/Login";
import Register from "./components/Register/Register";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import AdminHome from "./components/Home/Admin/AdminHome";
import Error from "./components/Error/Error";
import AdminUpload from "./components/Home/Admin/AdminUpload";
import AdminView from "./components/Home/Admin/AdminView";
import SurveyView from "./components/Home/Admin/SurveyView";
import AuthProvider, { useAuth } from "./components/Security/AuthContext";
import jwt_decode from "jwt-decode";
import AnnotatorView from "./components/Home/Annotator/AnnotatorView";
import AnnotationSurvey from "./components/Home/Annotator/AnnotationSurvey";

import AnnotatedView from "./components/Home/Admin/AnnotatedView";
import HighlightedView from "./components/Home/Admin/HighlightedView";

/**
 * This page is Brain of the Application where all the routes will be control for specific purpose.
 * Routes will be protected for specific user only. Like ADMIN can See only Admin parts and ANNOTATOR can see annotator parts.
 * @returns Render the page from where all routes will be shown with particular Components.
 */
function App() {
  const [currentForm, setCurrentForm] = useState("login");

  const changeForm = (form) => {
    setCurrentForm(form);
  };

  /**
   *
   * @param {*} param0 pass all children if user is login/register by checking jwtToken.
   * @returns All the children components.
   */
  function AuthenticatedRoute({ children }) {
    const authContext = useAuth();
    if (authContext.isAuthenticated || window.localStorage.getItem("jwtToken"))
      return children;
    else return <Navigate to="/" />;
  }

  /**
   *
   * @param {*} param0  Check whether user is Admin or not and only show admin components if it is true.
   * @returns Render all Children components for Admin only.
   */
  function IsAdminORNot({ children }) {
    const token = window.localStorage.getItem("jwtToken");
    if (token) {
      const decoded = jwt_decode(token.replace("Bearer", ""));
      if (decoded.Authority[0].authority === "ADMIN") return children;
    }
    return <div>USER NOT AUTHORIZED</div>;
  }

  /**
   *
   * @param {*} param0  Check whether user is Annotator or not and only show annotator components if it is true.
   * @returns Render all Children components for Annotator only.
   */
  function IsAnnotatorORNot({ children }) {
    const token = window.localStorage.getItem("jwtToken");
    if (token) {
      const decoded = jwt_decode(token.replace("Bearer", ""));
      if (decoded.Authority[0].authority === "ANNOTATOR") return children;
    }
    return <div>USER NOT AUTHORIZED</div>;
  }

  /**
   * Here Routes and Protected-Routes are implemented.
   */
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route
            path="/"
            element={
              window.localStorage.getItem("jwtToken") ? (
                jwt_decode(
                  window.localStorage.getItem("jwtToken").replace("Bearer", "")
                ).Authority[0].authority === "ADMIN" ? (
                  <Navigate to="admin/home" />
                ) : (
                  <Navigate to="/annotator/home/viewSurveys" />
                )
              ) : currentForm === "login" ? (
                <Login onFormSwitch={changeForm} />
              ) : (
                <Register onFormSwitch={changeForm} />
              )
            }
          />
          <Route
            path="/login"
            element={
              <AuthenticatedRoute>
                <Login />
              </AuthenticatedRoute>
            }
          ></Route>
          <Route
            path="/admin/home"
            element={
              <AuthenticatedRoute>
                <IsAdminORNot>
                  <AdminHome />
                </IsAdminORNot>
              </AuthenticatedRoute>
            }
          ></Route>
          <Route
            path="/admin/home/createSurvey"
            element={
              <AuthenticatedRoute>
                <IsAdminORNot>
                  <AdminUpload />
                </IsAdminORNot>
              </AuthenticatedRoute>
            }
          ></Route>
          <Route
            path="/admin/home/viewSurveys"
            element={
              <AuthenticatedRoute>
                <IsAdminORNot>
                  <AdminView />
                </IsAdminORNot>
              </AuthenticatedRoute>
            }
          ></Route>
          <Route
            path="/admin/home/viewSurveys/:survey_id"
            element={
              <AuthenticatedRoute>
                <IsAdminORNot>
                  <SurveyView />
                </IsAdminORNot>
              </AuthenticatedRoute>
            }
          />
          <Route
            path="/admin/home/viewAnnotatedSurveys/:survey_id"
            element={
              <AuthenticatedRoute>
                <IsAdminORNot>
                  <AnnotatedView />
                </IsAdminORNot>
              </AuthenticatedRoute>
            }
          />
          <Route
            path="/admin/home/viewHighlightedSurveys/:survey_id/snippet/:id"
            element={
              <AuthenticatedRoute>
                <IsAdminORNot>
                  <HighlightedView />
                </IsAdminORNot>
              </AuthenticatedRoute>
            }
          />

          <Route
            path="/annotator/home/viewSurveys"
            element={
              <AuthenticatedRoute>
                <IsAnnotatorORNot>
                  <AnnotatorView />
                </IsAnnotatorORNot>
              </AuthenticatedRoute>
            }
          />

          <Route
            path="/annotator/home/startSurvey"
            element={
              <AuthenticatedRoute>
                <IsAnnotatorORNot>
                  <AnnotationSurvey />
                </IsAnnotatorORNot>
              </AuthenticatedRoute>
            }
          />

          <Route
            path="/annotator/home/viewSurvey/startSurvey/:surveyId"
            element={
              <AuthenticatedRoute>
                <IsAnnotatorORNot>
                  <AnnotationSurvey />
                </IsAnnotatorORNot>
              </AuthenticatedRoute>
            }
          />
          <Route path="*" element={<Error />}></Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
