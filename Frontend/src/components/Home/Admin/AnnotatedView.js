import React, { useEffect, useState } from "react";
import Highlight from "../Highlight/Highlight";
import axios from "axios";
import { Button, Col, Container, ListGroup, Row } from "react-bootstrap";
import "../Admin/AnnotatedView.css";
import { useNavigate } from "react-router-dom";
import Header from "./AdminHeader";

/**
 * This Component is repsonsible to render all the surveys that are Annotated by Annotator.
 * @returns All surveys.
 */
const AnnotatedView = () => {
  const [snippets, setSnippets] = useState();
  const [showTags, setShowTags] = useState(false);
  const [taggedAnnotations, setTaggedAnnotations] = useState([]);
  const [selectedSnippetID, setSelectedSnippetID] = useState(null);

  const changePage = useNavigate();

  /**
   * Re-render the page by fetching all the snippets that are annotated by all Annotator.
   */
  useEffect(() => {
    const fetchData = async () => {
      const result = await axios.get(
        "/admin/" +
          window.localStorage.getItem("AdminUsername") +
          "/survey/" +
          window.localStorage.getItem("surveyID") +
          "/snippet/all/",
        {
          headers: {
            Authorization: window.localStorage.getItem("jwtToken"),
          },
        }
      );

      return result.data;
    };
    fetchData().then((res) => {
      console.log(res);
      setSnippets(res);
    });
  }, []);

  /**
   * View all tagged annotations with the particular snippets.
   * @param {*} snippetID is ID for snippet.
   */
  const handleTagsClick = (snippetID) => {
    setSelectedSnippetID(snippetID);
    if (selectedSnippetID !== null && snippetID !== selectedSnippetID) {
      setShowTags(true);
    }
    const fetchData = async () => {
      const result = await axios.get(
        "/admin/" +
          window.localStorage.getItem("AdminUsername") +
          "/survey/" +
          window.localStorage.getItem("surveyID") +
          "/snippet/" +
          snippetID +
          "/taggedAnnotations/all/",
        {
          headers: {
            Authorization: window.localStorage.getItem("jwtToken"),
          },
        }
      );
      return result.data;
    };
    fetchData().then((res) => {
      setTaggedAnnotations(res);
      console.log(res);
      if (selectedSnippetID === null || snippetID === selectedSnippetID) {
        setShowTags(!showTags);
      }
    });
  };

  /**
   * This function handle the click to show all highlighted snippets.
   * @param {*} snippetID Particular snippet.
   * @param {*} snippetText Snippet is passed further to show Pagination page.
   */
  const handleHighlightedClick = (snippetID, snippetText) => {
    const survey_id = window.localStorage.getItem("surveyID");
    const id = snippetID;
    const encodedSnippetText = encodeURIComponent(snippetText);
    changePage(
      `/admin/home/viewHighlightedSurveys/${survey_id}/snippet/${id}?text=${encodedSnippetText}`
    );
  };
  const handleBack = () => {
    changePage("/admin/home/viewSurveys ");
  };

  //Loader to show before data is fetch.
  if (!snippets) {
    return (
      <div className="loading-container">
        <div className="loading-spinner">
          <div className="loading-bar"></div>
          <div className="loading-bar"></div>
          <div className="loading-bar"></div>
          <div className="loading-bar"></div>
        </div>
        <div className="loading-text">Fetching...</div>
      </div>
    );
  }

  return (
    <Container fluid>
      <div className="header_surveyView">
        <button className="back_button" onClick={() => handleBack()}>
          Back
        </button>
        <h3>
          <h2 style={{ color: "rgb(7, 7, 77)" }}>Annotated Snippets</h2>
        </h3>
      </div>
      <ListGroup>
        {snippets.map((snippet) => (
          <ListGroup.Item key={snippet.snippetID}>
            <Row className="d-flex justify-content-end ">
              <Col sm={9}>
                <Highlight language="java" code={atob(snippet.snippetText)} />
              </Col>
              <Col sm={3}>
                <Button
                  className="aBtn1"
                  onClick={() => handleTagsClick(snippet.snippetID)}
                >
                  Tags
                </Button>
                <Button
                  className="aBtn2"
                  onClick={() =>
                    handleHighlightedClick(
                      snippet.snippetID,
                      snippet.snippetText
                    )
                  }
                >
                  Highlighted Tags
                </Button>
                {showTags && selectedSnippetID === snippet.snippetID && (
                  <div className="scrollable_window">
                    {taggedAnnotations.map((tags) => (
                      <h3>{tags.name}</h3>
                    ))}
                  </div>
                )}
              </Col>
            </Row>
          </ListGroup.Item>
        ))}
      </ListGroup>
    </Container>
  );
};

export default AnnotatedView;
