import React, { useEffect } from "react";
import Prism from "prismjs";
import "prismjs/themes/prism-okaidia.css";

import "prismjs/components/prism-java.min.js";
import "prismjs/plugins/line-numbers/prism-line-numbers.css";
import "prismjs/plugins/line-numbers/prism-line-numbers";
import "../Highlight/Highlight.css";

/**
 * This component is reponsible to do syntax hihglighting using PRISM.js library.
 * @param {*} param0 Language and code is passed from the page where code is to be syntax highlighted in particular language.
 * @returns
 */
const Highlight = ({ language, code }) => {
  /**
   * Highlight the snippet with prism.js
   */
  useEffect(() => {
    Prism.highlightAll();
  }, []);

  /**
   * Render prism.js converted code.
   */
  return (
    <pre className="render_view line-numbers">
      <code className={`language-${language}`}>{code}</code>
    </pre>
  );
};

export default Highlight;
