network option:
    #!/bin/bash
    case {{option}} in
        "up")
            set -e # exit on first error
            echo "🚀 Bringing up the network 🚀"
            cd fabric-samples/test-network && bash  network.sh up createChannel -ca
            echo "Deploy default chaincode "
            bash network.sh deployCC
            ;;
        "down")
            echo "🔥 Bringing down the network 🔥"
            cd fabric-samples/test-network && bash network.sh down
            ;;
        "restart")
            echo "🔥 Restarting the network 🔥"
            cd fabric-samples/test-network && bash network.sh down
            echo "🔥 Bringing up the network again 🔥"
            bash network.sh up
            ;;
        *)
            echo " just network up|down 👍"
            ;;
    esac