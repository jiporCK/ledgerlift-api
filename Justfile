network option:
    #!/bin/bash
    case {{option}} in
        "up")
            docker start 3f0b16319797
            set -e # exit on first error
            echo "🚀 Bringing up the network 🚀"
            cd fabric-samples/test-network && bash  network.sh up createChannel -ca
            echo "Deploy default chaincode "
            bash network.sh deployCC
            ;;
        "down")
            docker stop 3f0b16319797
            echo "🔥 Bringing down the network 🔥"
            cd fabric-samples/test-network && bash  network.sh down
            ;;
        *)
            echo " just network up|down 👍"
            ;;
    esac