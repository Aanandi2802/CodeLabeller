import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import admin from "../Home/Admin/AdminModel";
import annotator from "../Home/Annotator/AnnotatorModel";
import { useAuth } from "../Security/AuthContext";
import jwt_decode from "jwt-decode";
import "../Register/Register.css";
import { Button, Card, Col, Container, Form, Row } from "react-bootstrap";
import { TypeAnimation } from "react-type-animation";

const USER_REGEX = /^[A-Za-z][A-Za-z0-9_]{5,20}$/;
const PASSWORD_REGEX = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{4,15}$/;

const Register = (props) => {
  const [authority, setAuthority] = useState("");
  const [username, setUsername] = useState("");
  const [pwd, setPwd] = useState("");
  const [confirmPwd, setConfirmPwd] = useState("");
  const [error, setError] = useState([]);
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();
  const authContext = useAuth();

  var notify = () => {};
  const handleSubmit = (event) => {
    event.preventDefault();
    event.preventDefault();
    var er = validate(username, pwd, confirmPwd);
    setError(validate(username, pwd, confirmPwd));

    if (Object.keys(er).length === 0) {
      setLoading(true);
      axios
        .post(process.env.REACT_APP_API_REGISTER, {
          username: username,
          password: pwd,
          authority: authority,
        })
        .then((response) => {
          if (response.status === 200) {
            toast.success("Registration Successfull!");
            authContext.setAuthenticated(true);
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
          console.log(err);
          if (err.response?.status === 500) {
            toast.error("Server Error!");
            authContext.setAuthenticated(false);
            setTimeout(() => {
              setLoading(false);
              navigate("/");
            }, 1000);
          } else if (err.response?.status === 403) {
            toast.error("Account already created!");
            authContext.setAuthenticated(false);
            setTimeout(() => {
              setLoading(false);
              navigate("/");
            }, 1000);
          } else {
            toast.error("Registration Error!");
            authContext.setAuthenticated(false);
            setTimeout(() => {
              setLoading(false);
              navigate("/");
            }, 1000);
          }
        });
    }
  };

  const validate = (username, pwd, confirmPwd) => {
    const err = {};
    if (!username) {
      err.username = "Username is required!";
    } else if (!USER_REGEX.test(username)) {
      err.username = "Not valid Username format!";
    }
    if (!pwd) {
      err.pwd = "Password is required!";
    } else if (!PASSWORD_REGEX.test(pwd)) {
      err.pwd = "Not valid Password!";
    }
    if (!confirmPwd) {
      err.confirmPwd = "Re-type Password!";
    } else if (confirmPwd !== pwd) {
      err.confirmPwd = "Password not matching!";
    }
    return err;
  };

  const phrases = ["Code Labeler", 1000];
  const backspacePhrases = [
    "Code Labele",
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
            <div className="loading-text">Registering...</div>
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
                  color: "White",
                  whiteSpace: "nowrap",
                }}
              />
            </Col>
            <Col className="d-flex justify-content-center align-items-center">
              <Card className="mt-5 " style={{ borderRadius: "2rem" }}>
                <Card.Body>
                  <Card.Title
                    className="text-center"
                    style={{ color: "rgb(3, 3, 35)", fontWeight: "700" }}
                  >
                    Register
                  </Card.Title>
                  <Form onSubmit={handleSubmit} className="register_form">
                    <Form.Group controlId="authority">
                      <Form.Label className="mr-3">Authority: </Form.Label>
                      <Form.Check
                        required
                        type="radio"
                        label="ADMIN"
                        name="authority"
                        value="ADMIN"
                        inline
                        onChange={(e) => setAuthority(e.target.value)}
                        id="admin-radio"
                      />
                      <Form.Check
                        required
                        type="radio"
                        label="ANNOTATOR"
                        name="authority"
                        value="ANNOTATOR"
                        onChange={(e) => setAuthority(e.target.value)}
                        id="annotator-radio"
                        inline
                      />
                    </Form.Group>
                    <Form.Group controlId="username">
                      <Form.Label>Username</Form.Label>
                      <Form.Control
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        autoComplete="off"
                        type="text"
                        placeholder="Username"
                        name="username"
                      />
                      <Form.Text>{error.username}</Form.Text>
                    </Form.Group>
                    <Form.Group controlId="password">
                      <Form.Label>Password</Form.Label>
                      <Form.Control
                        value={pwd}
                        onChange={(e) => setPwd(e.target.value)}
                        type="password"
                        placeholder="*********"
                        name="password"
                      />
                      <Form.Text>{error.pwd}</Form.Text>
                    </Form.Group>
                    <Form.Group controlId="confirmPwd">
                      <Form.Label>ReType Password</Form.Label>
                      <Form.Control
                        value={confirmPwd}
                        onChange={(e) => setConfirmPwd(e.target.value)}
                        type="password"
                        placeholder="*********"
                        name="confirmPwd"
                      />
                      <Form.Text>{error.confirmPwd}</Form.Text>
                    </Form.Group>
                    <Button
                      className="button-only w-100 mt-4 "
                      style={{ borderRadius: "2rem" }}
                      type="submit"
                      onClick={notify}
                    >
                      Register
                    </Button>
                  </Form>
                  <Button
                    variant="link"
                    onClick={() => props.onFormSwitch("login")}
                    mt-2
                    style={{ color: "rgb(3, 3, 35)" }}
                  >
                    If you already have account! Login Here.
                  </Button>
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

export default Register;
