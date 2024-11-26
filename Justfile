network option:
    #!/bin/bash
    case {{option}} in
        "up")
            docker start 830cc6e4128b
            set -e # exit on first error
            echo "🚀 Bringing up the network 🚀"
            cd fabric-samples/test-network && bash  network.sh up createChannel -ca
            echo "Deploy default chaincode "
            bash network.sh deployCC
            ;;
        "down")
            docker stop 830cc6e4128b
            echo "🔥 Bringing down the network 🔥"
            cd fabric-samples/test-network && bash  network.sh down
            ;;
        *)
            echo " just network up|down 👍"
            ;;
    esac