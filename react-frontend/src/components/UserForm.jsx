import { TextField, Typography, Button } from "@mui/material"
import "../styles/Form.css"


const UserForm = (props) => {
    const title = props.title;
    const [firstId, secondId] = props.ids;
    const [firstState, setFirstState] = props.firstState;
    const [secondState, setSecondState] = props.secondState;
    const [firstType, secondType] = props.types; 
    const errorMsg = props.errorMsg;
    const onClick = props.onClick;


    return (
        <div className="login-container content-wrapper">
            <h2>{title}</h2>
            <div className="form-element">
                <TextField 
                    id={firstId}
                    value={firstState}
                    onChange={(e)=>{
                        setFirstState(e.target.value)
                        }}
                    label={firstId}
                    type={firstType}
                    variant="standard"
                    />
            </div>
            <div className="form-element">
                <TextField 
                    id={secondId}
                    value={secondState}
                    onChange={(e)=>{
                        setSecondState(e.target.value)
                        }}
                    label={secondId}
                    type={secondType}
                    variant="standard"
                />
            </div>
            <Typography style={{color: "red"}}>{errorMsg}</Typography>
            <div className="form-element">
                <Button variant="contained" onClick={onClick}>
                    {title}
                </Button>
            </div>
        </div>
    )
}

export default UserForm
