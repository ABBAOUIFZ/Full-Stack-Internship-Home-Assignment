package ma.dnaengineering.backend;


	import React, { useState } from 'react';
	import axios from 'axios';
	import { CSVLink } from 'react-csv';

	function App() {
	  const [file, setFile] = useState(null);
	  const [employees, setEmployees] = useState([]);
	  const [averageSalaryByJobTitle, setAverageSalaryByJobTitle] = useState({});

	  const handleFileChange = (event) => {
	    setFile(event.target.files[0]);
	  };

	    const handleProcessClick = () => {
	    const formData = new FormData();
	    formData.append('file', file);
	    axios.post('/api/employees', formData)
	      .then(response => {
	        setEmployees(response.data.employees);
	        setAverageSalaryByJobTitle(response.data.averageSalaryByJobTitle);
	      })
	      .catch(error => console.log(error));
	  };

	  return (
	    <div>
	      <div>
	        <label htmlFor="file">Upload CSV file:</label>
	        <input type="file" id="file" onChange={handleFileChange} />
	        {file && <button onClick={handleProcessClick}>Process</button>}
	      </div>
	      <div>
	        <h2>Employees</h2>
	        <table>
	          <thead>
	            <tr>
	              <th>ID</th>
	              <th>Name</th>
	              <th>Job Title</th>
	              <th>Salary</th>
	            </tr>
	          </thead>
	          <tbody>
	            {employees.map(employee => (
	              <tr key={employee.id}>
	                <td>{employee.id}</td>
	                <td>{employee.name}</td>
	                <td>{employee.jobTitle}</td>
	                <td>{employee.salary}</td>
	              </tr>
	            ))}
	          </tbody>
	        </table>
	      </div>
	      <div>
	        <h2>Job Title Summary</h2>
	        <table>
	          <thead>
	            <tr>
	              <th>Job Title</th>
	              <th>Average Salary</th>
	            </tr>
	          </thead>
	          <tbody>
	            {Object.entries(averageSalaryByJobTitle).map(([jobTitle, averageSalary]) => (
	              <tr key={jobTitle}>
	                <td>{jobTitle}</td>
	                <td>{averageSalary}</td>
	              </tr>
	            ))}
	          </tbody>
	        </table>
	        <CSVLink data={averageSalaryByJobTitle}>Download Job Title Summary</CSVLink>
	      </div>
	    </div>
	  );
	}

	export default App;
}
