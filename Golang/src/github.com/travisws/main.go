package main

import (
	"bufio"
	"fmt"
	"net"
	"strings"
    "os/exec"
)

//TODO Work on rest interface.
func main() {
	fmt.Println("Launching server...")
	// listen on all interfaces
	ln, _ := net.Listen("tcp", ":8081") // accept
	conn, _ := ln.Accept()              // run loop forever (or until ctrl-c)
	on := "on\n"
	message, _ := bufio.NewReader(conn).ReadString('\n')

	for {
		if message == on {
			newmessage := strings.ToUpper(message) // send new string back to client
			conn.Write([]byte(newmessage + "\n"))
			led()
		} else {
			conn.Write([]byte("Did not work\n"))
		}

	}

}

//Turns LED on and off
func led() {
	//exec.Command("led.py").Run()
	c := exec.Command("led.sh")

	if err := c.Run(); err != nil {
		fmt.Println("Error: ", err)
	}
}
