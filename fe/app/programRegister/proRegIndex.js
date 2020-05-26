import React, {Component}from 'react'
import ReactDOM from 'react-dom';
const axios = require('axios');
const regeneratorRuntime = require("regenerator-runtime");

import Customer from './Customer'
import Paper from '@material-ui/core/Paper';
import Table from '@material-ui/core/Table';
import TableHead from '@material-ui/core/TableHead';
import TableBody from '@material-ui/core/TableBody';
import TableRow from '@material-ui/core/TableRow';
import TableCell from '@material-ui/core/TableCell';
import CircularProgress from '@material-ui/core/CircularProgress';
import { withStyles } from '@material-ui/core/styles';


    

// class VoteIndex extends Component {
//     constructor(props){
//         super(props);
//     }
//     render() {
//        return (
                
//                     <a href={'/vote/result/'}>신청</a>
              

//             )
      
     
//     }
// }

const customers = [
    {
        'id' : 1,
        'userid' : 'cjy4044',
        'userpw' : '1234',
        'username' : '차재영',
        'nickname' : '차재',
        'gender' : '남자',
        'birth' : '961019',
        'profile' : 'https://placeimg.com/64/64/any',
        'phone' : '01040449984',
        'joindate' : '200521',
        'addr' : '대구광역시 남구 대명동',
        'addr2' : '3048-13 번지',
        'point' : 10,
        'role' : '1'
    },
    {
        'id' : 2,
        'userid' : 'cjy34',
        'userpw' : '1234',
        'username' : '차22',
        'nickname' : '차재',
        'gender' : '남자',
        'birth' : '961019',
        'profile' : 'https://placeimg.com/64/64/any',
        'phone' : '01040449984',
        'joindate' : '200521',
        'addr' : '대구광역시 남구 대명동',
        'addr2' : '3048-13 번지',
        'point' : 10,
        'role' : '1'
    }   ,
    {
        'id' : 3,
        'userid' : 'cjy1244',
        'userpw' : '12224',
        'username' : '차31재영',
        'nickname' : '차재',
        'gender' : '남자',
        'birth' : '961019',
        'profile' : 'https://placeimg.com/64/64/any',
        'phone' : '01040449984',
        'joindate' : '200521',
        'addr' : '대구광역시 남구 대명동',
        'addr2' : '3048-13 번지',
        'point' : 10,
        'role' : '1'
    }

]
  


    

class ProRegIndex extends Component {
    
    render() {
     
        const {classes} = this.props;

        return (
            <div>
                <div>
                <h1> RIRO 서비스를 신청하시겠습니까? </h1> <br/>
                </div>
                
                {/* <button ><VoteIndex>신청하기</VoteIndex></button> */}
                

                <div>
                {/* className={classes.root} */}
                  <Paper >
                  {/* className={classes.table} */}
                    <Table>
                    <TableHead>
                        <TableRow>
                                    <TableCell>번호</TableCell>
                                    <TableCell>이미지</TableCell>
                                    <TableCell>성명</TableCell>
                                    <TableCell>아이디</TableCell>
                                    <TableCell>비밀번호</TableCell>
                                    <TableCell>닉네임</TableCell>
                                    <TableCell>성별</TableCell>
                                    <TableCell>생년월일</TableCell>
                                    <TableCell>연락처</TableCell>
                                    <TableCell>가입일</TableCell>
                                    <TableCell>주소</TableCell>
                                    <TableCell>상세주소</TableCell>
                                    <TableCell>포인트</TableCell>
                                    <TableCell>권한번호</TableCell>
                         </TableRow>
                   </TableHead>
                   <TableBody>
                                {customers.map(c => {
                                return <Customer key={c.id}
                                id={c.id}
                                profile={c.profile}
                                username={c.username}
                                userid={c.userid}
                                userpw={c.userpw}
                                nickname={c.nickname}
                                gender={c.gender}
                                birth={c.birth}
                                phone={c.phone} 
                                joindate={c.joindate} 
                                addr={c.addr} 
                                addr2={c.addr2}
                                point={c.point}
                                role={c.role} 
                                />
                                })}
                       </TableBody>
              
                     </Table>
                     </Paper>

                     <div>
                <table className="register_table">
                    <tbody>
                        <tr>
                            <td><input className="register_input" type="text" id="id" name="id"  placeholder="사업자번호"  required/></td>
                            <td><input className="check_button"  type="button" value="중복확인"  /></td>
                            {/* onClick={this.checkId.bind(this)} */}
                        </tr>
                        <tr>
                            <td><input className="register_input" type="password" name="password" id="pwd" placeholder="비밀번호" required/></td>
                        </tr>
                        <tr>
                            <td><input className="register_input" type="password" name="password_check" id="pwd_check" placeholder="비밀번호 확인" required/></td>
                        </tr>
                        <tr>
                            <td colSpan="2"><hr></hr></td>
                        </tr>
                        <tr>
                            <td><input className="register_input" type="text" name="name" placeholder="이름"  required/></td>
                        </tr>
                        <tr>
                            <td><input className="register_birth" type="date" name="birth"  required/></td>
                        </tr>
                        <tr>
                            <td className="register_left_td"><input className="register_gender" type="radio" id="male" name="gender" value="0" required/>남자</td>
                            <td className="register_right_td"><input type="radio" id="female" name="gender" value="1" required/>여자</td>
                        </tr>
                        <tr>
                            <td colSpan="2"><hr></hr></td>
                        </tr>
                        <tr>
                        <td>
                            <input className="register_input" type="text" name="phone" required/></td>
                        {/* <td><input className="register_input" type="tel" name="phone" pattern="[0-9]{3}-[0-9]{4}-[0-9]{4}" placeholder="010-1234-1234" required/></td> */}
                        </tr>
                        <tr>
                            <td ><input className="register_input" type="text" name="addr" placeholder="도로명 주소" required/></td>
                            {/* <td colSpan="0.3"><input className="addr_input" type="text" name="addr" placeholder="시 도"/></td>
                            <td colSpan="0.3"><input className="addr_input" type="text" name="addr" placeholder="시 군 구"/></td>
                            <td colSpan="0.3"><input className="addr_input" type="text" name="addr" placeholder="동 명 읍"/></td> */}
                        </tr>
                        <tr>
                            <td><input className="register_input" type="text" name="addr2" placeholder="기타주소" required/></td>
                        </tr>
                        <tr>
                            <td><input className="register_input" type="text" name="nickname" placeholder="닉네임"  required/></td>
                        </tr>
                        
                    </tbody>
                </table>
                <button className="submit_button" type="submit">회원가입</button>
            </div>
            
                </div>
                             
             
            </div>
            
            
        )
   
      }

}

// export default withStyles(styles)(ProRegIndex);
export default ProRegIndex;

ReactDOM.render(<ProRegIndex/>,document.getElementById('proRegIndex'));