import { Typography } from "@mui/material"
import "../styles/PostForm.css"
import TagHandler from "./TagHandler";



const PostForm = (props) => {
    const header = props.header;
    const [title, setTitle] = props.title;
    const [content, setContent] = props.content;
    const [tags, setTags] = props.tags;
    const [errorMsg, setErrorMsg] = props.errorMsg;
    const handleSubmit = props.handleSubmit;

    return (
        <div className="form-div post-container">
            <h2>{header}</h2>
            <div className="form-div form">
                <div className="form-element">
                    <label htmlFor="title">Title:</label>
                    <input
                        type="text"
                        id="title"
                        name="title"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />

                    <label htmlFor="content">Content:</label>
                    <textarea
                        id="content"
                        name="content"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        rows="20"
                        required
                    ></textarea>

                    <TagHandler tags={[tags, setTags]} />
                </div>
                <Typography style={{color: "red"}}>{errorMsg}</Typography>
                <div className="form-div">
                    <button className="submit" onClick={handleSubmit}>Submit</button>
                </div>
            </div>
        </div>
  )
}

export default PostForm
