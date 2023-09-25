import React from "react";
import { Nav, Navbar } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../Security/AuthContext";
import "./AdminHeader.css";
import { ToastContainer, toast } from "react-toastify";

/**
 *
 * @param {*} props Passing title for the page
 * @returns Admin Header Component viewing the Home and Logout button
 */
const Header = (props) => {
  const authContext = useAuth();
  const changePage = useNavigate();

  /** 
    onLogout() function handles the logout button and logout the admin from the application.
  */
  const onLogout = () => {
    authContext.setAuthenticated(false);
    window.localStorage.clear();
    changePage(0);
    setTimeout(() => {
      changePage("/");
    }, 1000);
  };

  /** 
    onHome() functions handles the rendering of home page to Admin.
    If Admin is already on Home page then it will give toast with message of "Already at Home".
  */
  const onHome = () => {
    const currentPagePath = window.location.pathname;
    const targetPagePath = "/admin/home";
    if (currentPagePath === targetPagePath) {
      toast.success("This is Home page only!");
    }
    changePage("/admin/home");
  };

  return (
    /** 
      Navbar is code for showing the Navigation bar on frontend.
    */
    <Navbar expand="lg" variant="dark" className="custom-navbar">
      <Navbar.Brand>
        <img
          src="https://raw.githubusercontent.com/fabiospampinato/vscode-highlight/master/resources/logo.png"
          height="35"
          className="d-inline-block align-top"
          alt="Logo"
        />
        {props.title}
      </Navbar.Brand>

      <Nav className="ml-auto">
        <Nav.Link onClick={onHome}>Home</Nav.Link>
        <Nav.Link onClick={onLogout}>Logout</Nav.Link>
      </Nav>
      <ToastContainer />
    </Navbar>
  );
};

export default Header;
