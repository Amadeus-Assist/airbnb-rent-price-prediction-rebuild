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

import {Component} from "react";
import Menu from "./OuterContainer/Menu/Menu";
import Display from "./OuterContainer/Display/Display";

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
        <div className="App">
          {/*<header className="App-header">*/}
          {/*  <div className="App-intro">*/}
          {/*    <h2>Hello</h2>*/}
          {/*    <div>*/}
          {/*      {hello}*/}
          {/*    </div>*/}
          {/*  </div>*/}
          {/*</header>*/}
            <Menu/>
            <Display/>
        </div>
    );
  }
}
export default App;
