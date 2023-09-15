import { useState } from "react"
import "../styles/PostForm.css"
import { BASE_URL } from "../helper/connection";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import PostForm from "../components/PostForm";


const CreatePost = (props) => {
    const [me, setMe] = props.me;
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [tags, setTags] = useState([]);
    const [errorMsg, setErrorMsg] = useState("");
    const navigate = useNavigate();


    const handleSubmit = () => {
        if (title.trim() !=="" ) {
            const endpoint = BASE_URL + "posts/create";
            const newPost = {
                "title": title,
                "content": content,
                "createDate": new Date().toISOString(),
                "tags": tags
            }
            const config = {
                headers: {
                    Authorization: `Bearer ${me}`
                },
            }
            console.log(newPost);
            axios.post(endpoint, newPost, config)
                .then((res) => {
                    console.log("Submitted Post");
                    navigate("/");
                })
                .catch((err) => {
                    setErrorMsg("Something went wrong.")
                })
        } else {
            alert("Title cannot be empty")
        }
    }

    return (
        <PostForm 
            header="Create Post"
            title={[title, setTitle]}
            content={[content, setContent]}
            tags={[tags, setTags]}
            errorMsg={[errorMsg, setErrorMsg]}
            handleSubmit={handleSubmit}
        />
    )
}

export default CreatePost
