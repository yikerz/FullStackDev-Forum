import axios from "axios";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { BASE_URL } from "../helper/connection";
import UserForm from "../components/UserForm";
import "../styles/UserCard.css"
import useTokenGetUser from "../helper/useTokenGetUser";
import { deleteUser } from "../helper/deleteFunctions";

const DeleteUser = (props) => {
    const [me, setMe] = props.me;
    const [myUser, setMyUser] = useTokenGetUser(me);
    const [myUsername, setMyUsername] = useState("");
	const [myPassword, setMyPassword] = useState("");
    const { targetName } = useParams();
	const [errorMsg, setErrorMsg] = useState("");
	const navigate = useNavigate();
   
    const confirmDelete = async() => {
        const config = {
            auth: {
                username: myUsername,
                password: myPassword
            }
        };
        await deleteUser(targetName, config, setMe, () => setErrorMsg("Fail to delete account"));
        navigate("/");
    }

    return (
        <div>
            <h1>Confirm Delete Account</h1>
            <UserForm 
                title={"Delete Account"} 
                ids={["username", "password"]} 
                firstState={[myUsername, setMyUsername]} 
                secondState={[myPassword, setMyPassword]}
                types={["search", "password"]}
                errorMsg={errorMsg} 
                onClick={confirmDelete}
            />
        </div>
    )
}

export default DeleteUser
