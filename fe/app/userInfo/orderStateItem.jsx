import React from 'react';
export default class OrderStateItem extends React.Component {
    constructor(props){
        super(props)

    }
    render() {
        console.log(this.props)
        if(this.props.data.length != 0){
            return this.props.data.map((item, index) => {
                return (
                    <tbody className="bagItem" key={index}>
                        <tr>
                            <td rowSpan="2" className="item">
                                <span className="imgName">
                                    <a href={"/shop/product/"+item.product_id}>
                                        <img src={"/uploads/"+item.image}/>
                                    </a>
                                </span>
                            </td>
                            <td><a href={"/shop/product/"+item.product_id}>상품명: {item.name}</a></td>
                        </tr>
                        <tr>
                            <td>전체 판매개수: {item.sum}</td>
                        </tr>
                    </tbody>

                 )
            })
        }else{
            return <span>상품이 없습니다.</span>
        }
        
        
    }
}