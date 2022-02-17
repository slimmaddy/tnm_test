function fetchFromAPI() {
  const inpObj = document.getElementById("table");

  let requestOptions = {
    method: "GET",
    redirect: "follow",
  };

  fetch("http://localhost:8080/team-assignment", requestOptions)
    .then((response) => response.json())
    .then((res) => {
      if (res) {
        for (let item of res) {
          let trContent = document.createElement("tr");
          let cell0 = document.createElement("td");
          cell0.append(item.teamId);
          let cell1 = document.createElement("td");
          cell1.append(item.taskId);
          trContent.appendChild(cell0);
          trContent.appendChild(cell1);
          inpObj.appendChild(trContent);
        }
      }
    })
    .catch((error) =>
      console.log(
        "🚀 ~ file: javascript.js ~ line 29 ~ fetchFromAPI ~ error",
        error
      )
    );
}
