import './App.css';
import Navbar  from './components/Navbar';
import { Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Footer from './components/Footer';
import { useEffect, useState } from 'react';
import AllPosts from './pages/AllPosts';
import UserPage from './pages/UserPage';
import Register from './pages/Register';
import UpdatePassword from './pages/UpdatePassword';
import DeleteUser from './pages/DeleteUser';
import CreatePost from './pages/CreatePost';
import PostPage from './pages/PostPage';
import EditPost from './pages/EditPost';
import Error from './pages/Error';
import TagPage from './pages/TagPage';

function App() {
  const [me, setMe] = useState("");
  
  useEffect(() => {
    const tmpMe = localStorage.getItem("me")
    if (tmpMe === undefined) {
      return;
    } else {
      setMe(tmpMe);
    } 
  }, [me]) 

  return (
    <div className="App">
      <div className='main'>
        <Navbar me={[me, setMe]}/>
        <Routes>
          <Route path="*" element={<Error />} />
          <Route path="/" element={<AllPosts me={[me, setMe]} />} />
          <Route path="/login" element={<Login me={[me, setMe]}/>} />
          <Route path="/register" element={<Register me={[me, setMe]}/>} />
          <Route path="/user/:targetName" element={<UserPage me={[me, setMe]}/>} />
          <Route path="/update_password/:targetName" element={<UpdatePassword me={[me, setMe]}/>} />
          <Route path="/delete_user/:targetName" element={<DeleteUser me={[me, setMe]}/>} />
          <Route path="/post/create" element={<CreatePost me={[me, setMe]}/>} />
          <Route path="/post/:postId" element={<PostPage me={[me, setMe]} />} />
          <Route path="/post/:postId/edit" element={<EditPost me={[me, setMe]} />} />
          <Route path="/post/tag/:tagname" element={<TagPage me={[me, setMe]} />} />
          <Route path="/error" element={<Error />} />
        </Routes>
      </div>
      <Footer />
    </div>
  );
}

export default App;
