import { useEffect, useState } from "react";
import { BASE_URL } from "../helper/connection";
import axios from "axios";
import { FaPlusSquare, FaMinusSquare } from "react-icons/fa";
import "../styles/PostForm.css"
import { Button } from "@mui/material"
import ConfirmationModal from "./ConfirmationModal";

const TagHandler = (props) => {
    const [addedTags, setAddedTags] = props.tags
    const tagsEndpoint = BASE_URL + "posts/tags" 
    const [tags, setTags] = useState([]);
    const [tagNames, setTagNames] = useState([]);
    const [suggestedTags, setSuggestedTags] = useState([]);
    const [inputValue, setInputValue] = useState("");
    const [showModal, setShowModal] = useState(false);
    const maxTagNum = 5;

    useEffect(() => {
        let tmpTagNames = []
        axios.get(tagsEndpoint)
            .then((res) => {
                setTags(res.data);
                res.data.map((data) => {
                    tmpTagNames = [...tmpTagNames, data.name]
                })
                setTagNames(tmpTagNames);
                console.log("Retrieve all tags")
            }) 
            .catch((err) => {
                console.error(err);
            })
    }, [])
    
    const handleTagInputChange = (e) => {
        setInputValue(e.target.value);
        
        setSuggestedTags(
            tags.filter((tag) => 
                tag.name.toLowerCase().includes(e.target.value.toLowerCase())
    ))} 

    const handleTagAdd = () => {
        if (addedTags.length < maxTagNum) {
            const tagExistFilter = tags.filter((tag) => (tag.name === inputValue.trim()));
            if (tagExistFilter.length!==0) {
                const selectedTag = tagExistFilter[0];
                if (!addedTags.includes(selectedTag)) {
                    setAddedTags([...addedTags, selectedTag])            
                    console.log(`Tag (#${selectedTag.name}) is added to the list`)
                    setInputValue("");
                }
                else {
                    alert(`Tag (#${selectedTag.name}) is already in the list`)
                    setInputValue("");
                }
            } else {
                if (inputValue.trim().length !== 0) {
                    setShowModal(true)
                } else {
                    alert("Cannot insert empty tag.")
                }
            }
        } else {
            alert(`Maximum number of tags per post: ${maxTagNum}`);
        }
    }

    const handleTagRemove = () => {
        const filteredTags = addedTags.filter((addedTag) => (addedTag.name !== inputValue.trim()));
        setAddedTags(filteredTags);
        setInputValue("");  
    }

    const takeSuggestion = (tagname) => {
        setInputValue(tagname);
        setSuggestedTags([]);
    }

    const confirmCreateTag = () => {
        const createTagEndpoint = BASE_URL + `posts/createTag?tagname=${inputValue.trim()}`
        axios.post(createTagEndpoint)
            .then((res) => {
                console.log(`Tag (${res.data.name}) created`)
                setTags([...tags, res.data])
            })
            .catch((err) => {
                console.error(err);
            })
        setShowModal(false);
    }

    const cancelCreateTag = () => {
        console.log("Cancel tag creation")
        setShowModal(false);
    }

    return (
        <>
            <label htmlFor="tags">Tags:</label>
            {addedTags.length!==0
                ? <div className="added-tags">
                    <span>Added Tags: </span>{addedTags.map(addedTag => (
                        <span key={addedTag.id} className="added-tag">
                            <Button 
                                variant="outlined"
                                onClick={() => takeSuggestion(addedTag.name.trim())}
                            >
                                #{addedTag.name}
                            </Button >
                        </span>
                    ))}
                </div>
                : <></>
            }
            <div className="tag-input">
                <input
                    type="text"
                    id="tag"
                    name="tag"
                    value={inputValue}
                    onChange={(e) => handleTagInputChange(e)}
                    placeholder="Add tag..."
                />
                <FaPlusSquare className="add-button" onClick={handleTagAdd}/>
                <FaMinusSquare className="minus-button" onClick={handleTagRemove}/>
            </div>
            {inputValue.length !== 0 && suggestedTags.length !== 0
                ? <div className="suggested-tags">
                        <span>Suggested Tags: </span>{suggestedTags.map((suggestedTag) => (
                            <span key={suggestedTag.id} className="suggested-tag">
                                <Button 
                                    variant="outlined"
                                    onClick={() => takeSuggestion(suggestedTag.name.trim())}
                                >
                                    #{suggestedTag.name}
                                </Button >
                            </span>
                        ))}
                    </div>
            : <></>}
            {showModal && (
                <ConfirmationModal 
                    message={`Tag (#${inputValue}) does not exist, do you want to create it?`}
                    onConfirm={confirmCreateTag}
                    onCancel={cancelCreateTag}/>
            )}
        </>
    )
}

export default TagHandler
