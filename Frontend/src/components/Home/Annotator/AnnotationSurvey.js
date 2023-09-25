import React, { useEffect, useRef, useState } from "react";
import Prism from "prismjs";
import "prismjs/themes/prism-okaidia.css";
import "prismjs/components/prism-java.min.js";
import "../Annotator/AnnotationSurvey.css";
import { useNavigate } from "react-router-dom";
import AnnotatorHeader from "./AnnotatorHeader";
import { Button } from "react-bootstrap";

/**
 *
 * @returns Render the page where Annotator will annotate the surveys with all snippets.
 */
const AnnotationSurvey = () => {
  const preRef = useRef(null);
  const changePage = useNavigate();

  const [isLoading, setIsLoading] = useState(false);
  const [annotations, setAnnotations] = useState("Long Parameter");
  const [highlightedSpans, setHighlightedSpans] = useState([]);
  const [annotationIDS, setAnnotationIDS] = useState(null);
  const [checkboxName, setcheckBoxName] = useState([]);
  const [checkboxID, setcheckBoxID] = useState([]);
  const [annotatorHighlightTagResponse, setAnnotatorHighlightTagResponse] =
    useState(null);
  const [buttonClicked, setButtonClicked] = useState(0);

  const [snippetId, setSnippetId] = useState("");
  const [snippets, setSnippets] = useState(null);
  const [surveyAnnotations, setSurveyAnnotations] = useState([]);
  const [page, setPage] = useState(0);
  const [lastPage, setLastPage] = useState(false);

  const annotator_username = window.localStorage.getItem("AnnotatorUsername");
  const survey_id = window.localStorage.getItem("surveyID");

  /**
   * Responsible to map checkbox with particular name and annotationID.
   */
  const checkboxes = checkboxName.map((name, index) => {
    return {
      name: name,
      annotationID: checkboxID[index],
    };
  });

  /**
   * Re-render the page after user has highlighted the snippet and all data is then post after NEXT button is clicked.
   */
  useEffect(() => {
    console.log(JSON.stringify(annotatorHighlightTagResponse));
    setIsLoading(true);
    fetch(
      `/annotator/${annotator_username}/survey/${survey_id}/start/?page=${page}&snippetId=${snippetId}`,
      {
        method: "POST",
        mode: "cors",
        headers: {
          "Content-Type": "application/json",
          Authorization: window.localStorage.getItem("jwtToken"),
        },
        body: JSON.stringify(annotatorHighlightTagResponse),
      }
    )
      .then((response) => {
        return response.json();
      })
      .then((res) => {
        setSnippetId(res.content[0].snippetId);
        setSnippets(res.content[0].snippetText);
        setSurveyAnnotations(res.content[0].surveyAnnotationList);
        setPage(res.pageable.pageNumber + 1);
        setLastPage(res.last);
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {
        if (!lastPage) {
          setTimeout(() => {
            setIsLoading(false);
          }, 300);
        }
      });
  }, [buttonClicked]);

  /**
   * Re-render page after annotator select the highlighted portion with mouse.
   */
  useEffect(() => {
    const preElement = preRef.current;
    preElement.addEventListener("mouseup", handleMouseUp);
    return () => {
      preElement.removeEventListener("mouseup", handleMouseUp);
    };
  }, [annotations]);

  /**
   * Handles the mouse-up after Annotator has selected the particular snippets code.
   */
  const handleMouseUp = () => {
    const selectedText = window.getSelection().toString();

    const spans = preRef.current.getElementsByTagName("span");
    const selectedSpanIds = [];

    for (let i = 0; i < spans.length; i++) {
      const span = spans[i];
      if (selectedText.includes(span.textContent)) {
        if (window.getSelection().containsNode(span, true)) {
          selectedSpanIds.push(span.id);
        }
      }
    }
    selectedSpanIds.sort((a, b) => a - b);
    if (selectedSpanIds.length > 0) {
      const latestAnnotation = annotations.length > 0 ? annotations : "";
      setHighlightedSpans((prevSpans) => [
        ...prevSpans,
        {
          span_start_id: selectedSpanIds[0],
          span_end_id: selectedSpanIds[selectedSpanIds.length - 1],
          annotated_by: annotator_username,
          annotationResponse: {
            name: latestAnnotation,
            annotationID: annotationIDS,
          },
        },
      ]);
    }
  };

  /**
   * Handle the Annotation click for highlighting.
   * @param {*} param0 Annotation name and Id is set.
   */
  const handleAnnotationClick = ([annotation, ID]) => {
    setAnnotations(annotation);
    setAnnotationIDS(ID);
  };

  /**
   * Responsible to handle the checkbox of Annotations.
   * @param {*} param0 checked, name and ID of annotation is passed to checked the partiuclar annotation.
   */
  const handleAnnotationCheckBox = ([checked, name, id]) => {
    if (checked) {
      setcheckBoxName([...checkboxName, name]);
      setcheckBoxID([...checkboxID, id]);
    } else {
      const index = checkboxName.indexOf(name);
      if (index !== -1) {
        setcheckBoxName([
          ...checkboxName.slice(0, index),
          ...checkboxName.slice(index + 1),
        ]);
        setcheckBoxID([
          ...checkboxID.slice(0, index),
          ...checkboxID.slice(index + 1),
        ]);
      }
    }
  };

  const code = atob(snippets);
  let highlightedCode;
  highlightedCode = Prism.highlight(code, Prism.languages.java, "java");
  let modifiedCode = highlightedCode;

  let spanCounter = 0;

  /**
   * Span is added to the highlighted portion, so that further <mark> can be applied before and after span.
   */
  modifiedCode = highlightedCode.replace(/<span/g, () => {
    spanCounter++;
    return `<span id="${spanCounter}"`;
  });

  /**
   * <mark> tag is added to before and after <span> tag for particular SpanId so only that portion is highlighted.
   */
  highlightedSpans.forEach((spanGroup) => {
    const startSpanId = spanGroup.span_start_id;
    const endSpanId = spanGroup.span_end_id;
    const startSpanIndex = modifiedCode.indexOf(`id="${startSpanId}"`);
    const endSpanIndex =
      modifiedCode.indexOf(`id="${endSpanId}"`) + `id="${endSpanId}"`.length;
    const endSpanCloseIndex =
      modifiedCode.indexOf("</span>", endSpanIndex) + "</span>".length;

    modifiedCode = `${modifiedCode.slice(
      0,
      startSpanIndex - 6
    )}<mark>${modifiedCode.slice(
      startSpanIndex - 6,
      endSpanIndex - 7
    )}${modifiedCode.slice(
      endSpanIndex - 7,
      endSpanCloseIndex
    )}</span><span style="font-style:italic; color:rgb(175, 0, 0); font-size:0.9rem"> ${
      spanGroup.annotationResponse.name
    }</span></mark>${modifiedCode.slice(endSpanCloseIndex)}`;
  });

  /**
   * Handle the  clear button to clear all the highlighted portion.
   */
  const handleClear = () => {
    setAnnotations("Long Parameter");
    setHighlightedSpans([]);
    modifiedCode = highlightedCode;
  };

  /**
   * Handles the next button to save the al highlighted and annotation part for further sending POST request in useEffect.
   */
  const handleNext = () => {
    const file = {
      snippetId: snippetId,
      annotationResponseList: checkboxes,
      codeHighlightResponseList: highlightedSpans,
    };
    setAnnotatorHighlightTagResponse(file);
    setButtonClicked(buttonClicked + 1);
    setHighlightedSpans([]);
  };

  /**
   * Handle final submit of all the survey and after navigating to home page for Annotator.
   */
  const handleSubmit = () => {
    const file = {
      snippetId: snippetId,
      annotationResponseList: checkboxes,
      codeHighlightResponseList: highlightedSpans,
    };
    setAnnotatorHighlightTagResponse(file);
    setButtonClicked(buttonClicked + 1);
    setTimeout(() => {
      changePage("/annotator/home/viewSurveys");
    }, 2000);
  };

  return (
    <>
      {isLoading ? (
        <div className="loading-container">
          <div className="loading-spinner">
            <div className="loading-bar"></div>
            <div className="loading-bar"></div>
            <div className="loading-bar"></div>
            <div className="loading-bar"></div>
          </div>
          {lastPage ? (
            <div className="loading-text">Submitting...</div>
          ) : (
            <div className="loading-text">Loading...</div>
          )}
        </div>
      ) : (
        <>
          <AnnotatorHeader title="Annotate Page" />
          <div className="main_start">
            <div className="snippets_start">
              <pre className="render_view" ref={preRef}>
                <code
                  className="language-java"
                  dangerouslySetInnerHTML={{ __html: modifiedCode }}
                ></code>
              </pre>
            </div>
            <div className="annotations_start">
              <header style={{ color: "blue", fontWeight: "bold" }}>
                SELECT ANNOTATION
              </header>
              <div className="annotations_buttons">
                <div className="buttons_annotate">
                  {surveyAnnotations.map((annotation) => (
                    <Button
                      className="annotate_buttons"
                      onClick={() =>
                        handleAnnotationClick([
                          annotation.name,
                          annotation.annotationID,
                        ])
                      }
                    >
                      {annotation.name}
                    </Button>
                  ))}
                </div>
              </div>
              <Button className="clearAll" onClick={() => handleClear()}>
                CLEAR ALL
              </Button>
              <header style={{ color: "blue", fontWeight: "bold" }}>
                CHOICE ANNOTATIONS
              </header>
              <div className="annotations_options">
                {surveyAnnotations.map((annotation) => (
                  <>
                    <li>
                      <input
                        type="checkbox"
                        id={annotation.ID}
                        value={annotation.name}
                        onChange={(e) =>
                          handleAnnotationCheckBox([
                            e.target.checked,
                            annotation.name,
                            annotation.annotationID,
                          ])
                        }
                      />
                      <label className="option_tags" htmlFor={annotation.ID}>
                        &nbsp;{annotation.name}
                      </label>
                    </li>
                  </>
                ))}
              </div>
              {lastPage ? (
                <Button onClick={() => handleSubmit()}>Submit</Button>
              ) : (
                <Button onClick={() => handleNext()}>Next</Button>
              )}
            </div>
          </div>
        </>
      )}
    </>
  );
};

export default AnnotationSurvey;
