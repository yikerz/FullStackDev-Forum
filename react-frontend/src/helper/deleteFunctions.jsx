import axios from "axios"
import { BASE_URL } from "./connection";

export const deleteUser = async(targetName, config, setMe, errorCallback) => {
    const endpoint = BASE_URL + "users/delete?username=" + targetName;
    await axios.delete(endpoint, config)
        .then((res) => {
            console.log(`Account (${targetName}) is deleted.`);
            if (setMe !== undefined) {
                setMe("");
                localStorage.removeItem("me");
            }
        })
        .catch((err) => {
            if (errorCallback !== undefined) {
                errorCallback(err);
            } else {
                console.error("Fail to delete user: " + targetName);
            }
        })
}

export const deletePost = async(postId, config) => {
    const endpoint = BASE_URL + "posts/delete?id=" + postId;
    await axios.delete(endpoint, config)
        .then((res) => {
            console.log(`Post (id: ${postId}) is deleted`);
        })
        .catch((err) => {
            console.error(`Fail to delete Post (id: ${postId})`);
        })
}

export const deleteComment = async(commentId, postId, config) => {
    const endpoint = BASE_URL + `posts/${postId}/comments/delete?id=${commentId}`;
    await axios.delete(endpoint, config)
        .then((res) => {
            console.log(`Comment (id: ${commentId}) is deleted`);
        })
        .catch((err) => {
            console.error(`Fail to delete Comment (id: ${commentId})`);
        })
}