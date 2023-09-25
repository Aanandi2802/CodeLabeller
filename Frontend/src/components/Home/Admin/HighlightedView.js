import React from "react";
import Prism from "prismjs";
import "prismjs/components/prism-java.min.js";
import { useEffect } from "react";
import { useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import ReactPaginate from "react-paginate";
import "../Admin/HighlightedView.css";
import { Button } from "react-bootstrap";

/**
 *
 * @param {*} props
 * @returns Render the Pagination page to show all the highlighted snippets one by one.
 */
const HighlightedView = (props) => {
  const { id } = useParams();
  const { survey_id } = useParams();

  const changePage = useNavigate();
  const location = useLocation();

  //Getting all the snippets by decoding from URL.
  const snippets = decodeURIComponent(
    new URLSearchParams(location.search).get("text")
  );

  const [highlightedData, setHighlightedData] = useState();
  const [page, setPage] = useState(0);
  const [mmodifiedCode, setModifiedCode] = useState();
  const [totalPages, setTotalPages] = useState();

  /**
   * Re-rendert the page by fetching one-by-one all the highlighted snippets.
   */
  useEffect(() => {
    const fetchData = async () => {
      const result = await axios.get(
        "/admin/" +
          window.localStorage.getItem("AdminUsername") +
          "/survey/" +
          survey_id +
          "/snippet/" +
          id +
          "/highlightResponses/start/?page=" +
          page,
        {
          headers: {
            Authorization: window.localStorage.getItem("jwtToken"),
          },
        }
      );

      return result.data;
    };
    fetchData().then((res) => {
      setHighlightedData(res.content[0]);
      setTotalPages(res.totalPages);
    });
  }, [page]);

  /**
   * Re-rendering page by converting the code into JAVA-PRISM and Highlighted Form.
   */
  useEffect(() => {
    const code = atob(snippets);
    let highlightedCode;
    highlightedCode = Prism.highlight(code, Prism.languages.java, "java");
    let modifiedCode = highlightedCode;

    let spanCounter = 0;
    modifiedCode = highlightedCode.replace(/<span/g, () => {
      spanCounter++;
      return `<span id="${spanCounter}"`;
    });

    if (highlightedData) {
      highlightedData.forEach((spanGroup) => {
        const startSpanId = spanGroup.span_start_id;
        const endSpanId = spanGroup.span_end_id;
        const startSpanIndex = modifiedCode.indexOf(`id="${startSpanId}"`);
        const endSpanIndex =
          modifiedCode.indexOf(`id="${endSpanId}"`) +
          `id="${endSpanId}"`.length;
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
    }
    setModifiedCode(modifiedCode);
  }, [highlightedData]);
  const handlePageClick = (data) => {
    setPage(data.selected);
  };

  /**
   * Handles the back button for going back to Viewing all surveys.
   */
  const onbackToHandle = () => {
    changePage(`/admin/home/viewAnnotatedSurveys/${survey_id}`);
  };

  if (!highlightedData) {
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
    <div>
      <div className="paginate">
        <Button
          className="back_button1"
          onClick={() => {
            onbackToHandle();
          }}
        >
          BACK
        </Button>
        <ReactPaginate
          previousLabel={"previous"}
          nextLabel={"next"}
          breakLabel={"..."}
          pageCount={totalPages}
          marginPagesDisplayed={3}
          onPageChange={handlePageClick}
          containerClassName={"pagination justify-content-center"}
          pageClassName={"page-item"}
          pageLinkClassName={"page-link"}
          previousClassName={"page-item"}
          previousLinkClassName={"page-link"}
          nextClassName={"page-item"}
          nextLinkClassName={"page-link"}
          breakClassName={"page-item"}
          breakLinkClassName={"page-link"}
          activeClassName={"active"}
        />
      </div>
      <pre className="render_view">
        <code
          className="language-java"
          dangerouslySetInnerHTML={{ __html: mmodifiedCode }}
        ></code>
      </pre>
    </div>
  );
};

export default HighlightedView;
