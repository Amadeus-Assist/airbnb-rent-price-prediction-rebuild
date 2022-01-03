import React, {Component} from 'react';
import Menu from "./Menu/Menu";
import Display from "./Display/Display";

class OuterContainer extends Component {
    render() {
        return (
            <div className="ui vertical masthead segment">
                <Menu/>
                <Display/>
            </div>
        );
    }
}

export default OuterContainer;