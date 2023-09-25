import axios from "axios";
import React from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import admin from "../Home/Admin/AdminModel";
import annotator from "../Home/Annotator/AnnotatorModel";
import { useAuth } from "../Security/AuthContext";
import jwt_decode from "jwt-decode";
import { TypeAnimation } from "react-type-animation";
import "../Login/Login.css";
import { Container, Form, Button, Card, Row, Col } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";

const Login = (props) => {
  const [username, setUsername] = useState("");
  const [pwd, setPwd] = useState("");
  const [loading, setLoading] = useState(false);
  const authContext = useAuth();
  const navigate = useNavigate();

  var notify = () => {};
  const handleSubmit = (event) => {
    event.preventDefault();
    setLoading(true);
    axios
      .post(process.env.REACT_APP_API_LOGIN, {
        username: username,
        password: pwd,
      })
      .then((response) => {
        if (response.status === 200) {
          authContext.setAuthenticated(true);
          toast.success("Login Successfull!");
          const decodedToken = jwt_decode(response.data.jwtToken);
          window.localStorage.setItem(
            "jwtToken",
            "Bearer " + response.data.jwtToken
          );

          setTimeout(() => {
            setLoading(false);
            if (decodedToken.Authority[0].authority === "ADMIN") {
              admin.name = username;
              window.localStorage.setItem("AdminUsername", username);
              navigate("/admin/home");
            } else {
              annotator.name = username;
              window.localStorage.setItem("AnnotatorUsername", username);
              navigate("/annotator/home/viewSurveys");
            }
          }, 1000);
        }
      })
      .catch((err) => {
        if (err.response?.status === 500) {
          authContext.setAuthenticated(false);
          toast.error("Server Error");
          setLoading(false);
          navigate("/");
        } else {
          authContext.setAuthenticated(false);
          toast.error("Wrong crendentials. Please Login again!");
          setLoading(false);
          navigate("/");
        }
      });
  };
  const phrases = ["Code Labeller", 1000];
  const backspacePhrases = [
    "Code Labelle",
    "Code Labell",
    "Code Label",
    "Code Labe",
    "Code Lab",
    "Code La",
    "Code L",
    "Code ",
    "Code",
    "Cod",
    "Co",
    "C",
    " ",
    1000,
  ];

  return (
    <body className={`${loading ? "loading-bg" : "lr"}`}>
      <Container>
        {loading ? (
          <div className="loading-container">
            <div className="loading-spinner">
              <div className="loading-bar"></div>
              <div className="loading-bar"></div>
              <div className="loading-bar"></div>
              <div className="loading-bar"></div>
            </div>
            <div className="loading-text">Verifying...</div>
          </div>
        ) : (
          <Row className="h-100">
            <Col
              md={8}
              className="d-flex justify-content-center align-items-center"
            >
              <TypeAnimation
                sequence={phrases.concat(backspacePhrases)}
                repeat={Infinity}
                cursor={{ hideWhenDone: true }}
                style={{
                  fontSize: "5em",
                  display: "inline-block",
                  color: "white",
                  whiteSpace: "nowrap",
                }}
              />
            </Col>
            <Col className="d-flex justify-content-center align-items-center">
              <Card className="mt-5" style={{ borderRadius: "2rem" }}>
                <Card.Body>
                  <Card.Title
                    className="text-center"
                    style={{ color: "rgb(3, 3, 35)", fontWeight: "700" }}
                  >
                    Login
                  </Card.Title>
                  <Form onSubmit={handleSubmit}>
                    <Form.Group controlId="username">
                      <Form.Label>Username</Form.Label>
                      <Form.Control
                        required
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        type="text"
                        placeholder="Username"
                      />
                    </Form.Group>
                    <Form.Group controlId="password">
                      <Form.Label>Password</Form.Label>
                      <Form.Control
                        required
                        value={pwd}
                        onChange={(e) => setPwd(e.target.value)}
                        type="password"
                        placeholder="*********"
                      />
                    </Form.Group>
                    <Button
                      className="button-only w-100 mt-4 "
                      style={{ borderRadius: "2rem" }}
                      variant="primary"
                      type="submit"
                      onClick={notify}
                    >
                      Log In
                    </Button>
                  </Form>
                  <div className="text-center mt-3">
                    <Button
                      variant="link"
                      onClick={() => props.onFormSwitch("register")}
                      mt-2
                      style={{ color: "rgb(3, 3, 35)" }}
                    >
                      Don't have an account? Register here.
                    </Button>
                  </div>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        )}
        <ToastContainer />
      </Container>
    </body>
  );
};

export default Login;
