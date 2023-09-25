import "../Admin/AdminUpload.css";
import React, { useState } from "react";
import { FiLogOut } from "react-icons/all";
import { AiFillDelete, AiOutlineUpload } from "react-icons/all";
import admin from "./AdminModel";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import { useAuth } from "../../Security/AuthContext";
import Highlight from "../Highlight/Highlight";
import Header from "./AdminHeader";
import { Container } from "react-bootstrap";

/**
 *
 * @returns This page renders the page to upload the snippets for admin with different annotations.
 */
const AdminUpload = () => {
  const [option, setOption] = useState("Java");
  const [fileView, setFileView] = useState([]);
  const [annotate, setAnnotate] = useState([]);
  const [frontview, setFrontView] = useState([]);

  const authContext = useAuth();
  const changePage = useNavigate();

  const admin_name = admin.name;

  /**
   *
   * @param {
   * } event handles all the data that is uploaded by the admin such as snippets, annotations,survey name, surveyThreshod...
   */
  const onSubmit = (event) => {
    let surveyName = document.getElementById("surveyName").value;
    let surveyLanguage = option;
    let snippets = fileView;
    let annotations = annotate;
    let surveyThreshold = document.getElementById("surveyThreshold").value;

    //Object to pass as JSON in backend for all data.
    let fileObject = {
      surveyName,
      surveyLanguage,
      snippets,
      annotations,
      surveyThreshold,
    };

    event.preventDefault();
    //Posting all the data to backend.
    fetch(process.env.REACT_APP_API_CREATE_SURVEY + admin_name + "/survey/", {
      method: "POST",
      mode: "cors",
      headers: {
        "Content-Type": "application/json",
        Authorization: window.localStorage.getItem("jwtToken"),
      },
      body: JSON.stringify(fileObject),
    })
      .then((response) => {
        console.log(response.status);
        if (response.status === 200) toast.success("Submitted Successfully!");
        setTimeout(() => {
          changePage("/admin/home/viewSurveys");
        }, 1000);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  /**
   *
   * @param {*} file Contain the file that needs to convert into compressed version of Base64 so that backend can easily store.
   * @returns Converted file in Base64 format.
   */
  const convertBase64 = (file) => {
    return new Promise((resolve, reject) => {
      const fileReader = new FileReader();
      fileReader.readAsDataURL(file);
      fileReader.onload = () => {
        resolve(fileReader.result);
      };

      fileReader.onerror = (err) => {
        reject(err);
      };
    });
  };

  let allFiles = [];
  let allFilesTemp = [];
  let allFilesEncode = [];

  /**
   * Responsible to re-render the uploaded files after converting it into Base64.
   * @param {*} e Has all the uploaded data that is to be re-render to Admin just to see again.
   */
  const onUpload = async (e) => {
    const filePath = [];
    const arr = Array.from(e.target.files);

    arr.forEach((element) => {
      filePath.push(convertBase64(element));
    });

    allFilesTemp = await Promise.all(filePath);
    allFilesTemp.forEach((e) => {
      const temp = e.replace(/^data:(.*,)?/, "");
      allFiles.push(temp);
      const decodedText = atob(temp);
      console.log(decodedText);
      allFilesEncode.push(decodedText);
    });

    setFileView(allFiles);
    setFrontView(allFilesEncode);
  };

  /**
   * This function add new annotation input tab.
   */
  const handleAdd = () => {
    const val = [...annotate, []];
    setAnnotate(val);
  };

  /**
   * This Function store the annotations name to further send it to backend.
   * @param {*} event Name of annotation to store.
   * @param {*} i Index of annotation where it is to be store.
   */
  const handleChange = (event, i) => {
    const annotateInput = [...annotate];
    annotateInput[i] = event.target.value;
    setAnnotate(annotateInput);
  };

  /**
   * This Function handles delete of annotation if Admin remove it after entering.
   * @param {*} d Index of annotations which is to be delete.
   */
  const handleDelete = (d) => {
    const deleteAnnotate = [...annotate];
    deleteAnnotate.splice(d, 1);
    setAnnotate(deleteAnnotate);
  };

  let imgGallery = frontview;

  return (
    <>
      <Header title="Survey Create Page" />
      <div className="snippets">
        <p className="annotate_p">Upload Code Snippets</p>
      </div>
      <div className="annotations">
        <p className="annotate_p">Create Annotations</p>
      </div>
      <div className="lang">
        <label className="label_s">Survey Name:</label>
        <input
          required
          id="surveyName"
          type="text"
          className="survey_input"
          size="7"
        ></input>
        <label className="label_s">Language</label>
        <select className="option" onChange={(e) => setOption(e.target.value)}>
          <option value="Java">Java</option>
          <option>Python</option>
          <option value="Javascript">javascript</option>
        </select>
        <label className="label_s">Threshold:</label>
        <input
          id="surveyThreshold"
          type="text"
          className="surveyThreshold"
        ></input>
        <label htmlFor="file-upload" className="custom-file-upload">
          <AiOutlineUpload style={{ fontSize: "1.5rem" }} />
          <span style={{ color: "rgba(11, 6, 72, 0.927)", fontWeight: "500" }}>
            Snippets
          </span>
        </label>
        <input
          id="file-upload"
          className="file"
          type="file"
          multiple
          onChange={onUpload}
        ></input>
        {imgGallery.length > 0 ? (
          <div className="uploaded_snippets">
            {imgGallery.map((index) => (
              <Highlight language="java" code={index} />
            ))}
          </div>
        ) : null}
      </div>
      <div className="uploaded_annotations">
        <button className="add" onClick={() => handleAdd()}>
          Add
        </button>
        {annotate.map((data, i) => {
          return (
            <div className="add_portion">
              <input
                className="annotate_input"
                value={data}
                onChange={(event) => handleChange(event, i)}
              />
              <button
                className="delete_annotate"
                onClick={() => handleDelete(i)}
              >
                <AiFillDelete />
              </button>
            </div>
          );
        })}
        <button className="submit" onClick={onSubmit}>
          Submit
        </button>
      </div>
      <ToastContainer />
    </>
  );
};

export default AdminUpload;
