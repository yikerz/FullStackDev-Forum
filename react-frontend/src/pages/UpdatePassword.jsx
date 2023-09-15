import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import { BASE_URL } from "../helper/connection";
import UserForm from "../components/UserForm";
import { useNavigate } from "react-router-dom";

const UpdatePassword = (props) => {
    const [me, setMe] = props.me;
    const { targetName } = useParams();
    const [errorMsg, setErrorMsg] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmNewPassword, setConfirmNewPassword] = useState("");
    const navigate = useNavigate();


    const handleChangePassword = () => {
        if (newPassword === confirmNewPassword) {
            const endpoint = BASE_URL + "users/changePassword?password=" + newPassword;
            const config = {
                headers: {
                    Authorization: `Bearer ${me}`
                },
            }
            console.log(config)
            axios.put(endpoint, {}, config)
                .then((res) => {
                    alert("Password changed successfully! Please login again.");
                    setMe("");
                    localStorage.removeItem("me");
                    navigate("/login");
                })
        }
        else {
            setErrorMsg("Passwords do not match. Please try again.")
        }
    }

    return (
        <UserForm 
            title={"Change Password"} 
            ids={["New Password", "Confirm Password"]} 
            firstState={[newPassword, setNewPassword]} 
            secondState={[confirmNewPassword, setConfirmNewPassword]}
            errorMsg={errorMsg}
            types={["password", "password"]}
            onClick={handleChangePassword}
        />
    )
}

export default UpdatePassword
