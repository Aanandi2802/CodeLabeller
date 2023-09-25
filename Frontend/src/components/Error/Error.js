import React from "react";

/**
 *
 * @returns This component is responsible to show error if random URL is hitted.
 */
const Error = () => {
  return (
    <div className="ErrorComponent">
      <h1>Oops!</h1>
      <div className="errorMessage">
        We can't seem to find the page you are looking for.
      </div>
    </div>
  );
};

export default Error;
