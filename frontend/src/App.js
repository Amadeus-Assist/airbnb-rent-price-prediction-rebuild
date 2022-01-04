//
// function App() {
//   return (
//     <div className="App">
//       <header className="App-header">
//         <img src={logo} className="App-logo" alt="logo" />
//         <p>
//           Edit <code>src/App.js</code> and save to reload.
//         </p>
//         <a
//           className="App-link"
//           href="https://reactjs.org"
//           target="_blank"
//           rel="noopener noreferrer"
//         >
//           Learn React
//         </a>
//       </header>
//     </div>
//   );
// }
//
// export default App;

import React, {Component} from "react";
import Menu from "./components/Menu/Menu";
import Display from "./components/Display/Display";

class App extends Component {
  // state = {
  //   hello: ""
  // };
  //
  // async componentDidMount() {
  //   const response = await fetch('/api/hello');
  //   const body = await response.text();
  //   this.setState({hello: body});
  //   console.log(body)
  // }

  render() {
    // const {hello} = this.state;
    return (
        <div className="App" >
          {/*<header className="App-header">*/}
          {/*  <div className="App-intro">*/}
          {/*    <h2>Hello</h2>*/}
          {/*    <div>*/}
          {/*      {hello}*/}
          {/*    </div>*/}
          {/*  </div>*/}
          {/*</header>*/}
            <div className="ui vertical masthead segment">
                <Menu/>
                <Display/>
            </div>

        </div>
    );
  }
}
export default App;
