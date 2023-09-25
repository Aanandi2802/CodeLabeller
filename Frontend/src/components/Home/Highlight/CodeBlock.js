import React, { useEffect, useRef, useState } from "react";
import Prism from "prismjs";

import "prismjs/components/prism-java.min.js";
import "../Highlight/Highlight.css";

/**
 * This component is only for testing not the working component.
 * @param {} param0
 * @returns
 */
const CodeBlock = ({ code }) => {
  const preRef = useRef(null);

  const [annotations, setAnnotations] = useState("Long Parameter");
  const [highlightedSpans, setHighlightedSpans] = useState([]);

  useEffect(() => {
    const preElement = preRef.current;
    preElement.addEventListener("mouseup", handleMouseUp);
    return () => {
      preElement.removeEventListener("mouseup", handleMouseUp);
    };
  }, [annotations]);

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
          ids: [
            selectedSpanIds[0],
            selectedSpanIds[selectedSpanIds.length - 1],
          ],
          an: latestAnnotation,
        },
      ]);
    }
  };
  const handleAnnotationClick = (annotation) => {
    setAnnotations(annotation);
  };

  const highlightedCode = Prism.highlight(code, Prism.languages.java, "java");
  let modifiedCode = highlightedCode;

  let spanCounter = 0;
  modifiedCode = highlightedCode.replace(/<span/g, () => {
    spanCounter++;
    return `<span id="${spanCounter}"`;
  });

  highlightedSpans.forEach((spanGroup) => {
    const startSpanId = spanGroup.ids[0];
    const endSpanId = spanGroup.ids[1];
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
    )}</span><span style="font-style:italic; color:blue; font-size:medium"> ${
      spanGroup.an
    }</span></mark>${modifiedCode.slice(endSpanCloseIndex)}`;
  });
  const handleSubmit = () => {
    console.log(highlightedSpans);
  };
  const handleClear = () => {
    setAnnotations("Long Parameter");
    setHighlightedSpans([]);
    modifiedCode = highlightedCode;
  };
  return (
    <div className="main">
      <div className="annotate_here">
        <pre className="render_view" ref={preRef}>
          <code
            className="language-java"
            dangerouslySetInnerHTML={{ __html: modifiedCode }}
          ></code>
        </pre>
      </div>
      <div className="buttons_annotate">
        <button onClick={() => handleAnnotationClick("Long Parameter")}>
          Long Parameter
        </button>
        <button onClick={() => handleAnnotationClick("Magic Number")}>
          Magic Number
        </button>
        <button onClick={() => handleAnnotationClick("Architecture Smell")}>
          Architecture Smell
        </button>
        <button onClick={() => handleAnnotationClick("wrong variable")}>
          wrong variable"
        </button>
        <button onClick={() => handleSubmit()}>SUBMIT</button>
        <button onClick={() => handleClear()}>CLEAR ALL</button>
      </div>
    </div>
  );
};

export default CodeBlock;
